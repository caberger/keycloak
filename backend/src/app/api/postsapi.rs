use actix_web::{get, HttpResponse};
use actix_web_middleware_keycloak_auth::{KeycloakClaims, KeycloakRoles};
use crate::app::api::claims::{self, is_user_in_role, ClaimsFromJwt};
use crate::app::api::constants::APPLICATION_JSON;
use crate::app::db::dbpool::get_connection;
use crate::models::Post;
use crate::schema::post::dsl::*;
use diesel::prelude::*;

/** @return either all posts if user is an editor or published only.
 * Demonstrates 2 ways to get the roles and shows how to extract info from jwt.
 */
#[get("/posts")]
pub async fn get(claims: KeycloakClaims<ClaimsFromJwt>, roles: KeycloakRoles) -> HttpResponse {
    let published_only = !is_user_in_role(roles, claims::EDITOR_ROLE);
    let mut con = get_connection().unwrap();
    println!("{} allowed to see unpublished posts: {}", claims.preferred_username, !published_only);

    let stmt = post.select(Post::as_select());
    let mut results = stmt.load(&mut con);
    if published_only {
        results = stmt.filter(published.eq(published_only)).load(&mut con);
    }
    HttpResponse::Ok()
        .content_type(APPLICATION_JSON)
        .json(results.expect("Error loading posts"))
}
