use actix_web::{get, HttpResponse};
use actix_web_middleware_keycloak_auth::KeycloakClaims;
use backend::establish_connection;
use crate::{claims::{ClaimsFromJwt, EDITOR_ROLE}, constants::APPLICATION_JSON, models::Post, schema };
use schema::posts::dsl::*;
use diesel::prelude::*;

#[get("/posts")]
pub async fn get(claims: KeycloakClaims<ClaimsFromJwt>) -> HttpResponse {
    let roles = &claims.realm_access.roles;
    let published_only = !roles.contains(&String::from(EDITOR_ROLE));
    println!("{} allowed to see unpublished posts: {}", claims.preferred_username, !published_only);
    load(published_only)
}
fn load(published_only: bool) -> HttpResponse {
    let connection = &mut establish_connection();
    let stmt = posts
        .limit(50)
        .select(Post::as_select());

    let all_results = stmt;
    let filtered_results =
        stmt
        .filter(published.eq(published_only));
    let results =
        if published_only {
            filtered_results.load(connection)
        } else {
            all_results.load(connection)
        }
        .expect("Error loading posts")
    ;
    HttpResponse::Ok()
        .content_type(APPLICATION_JSON)
        .json(results)
}

