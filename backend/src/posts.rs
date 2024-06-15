use actix_web::{get, HttpResponse};
use backend::establish_connection;
use crate::{constants::APPLICATION_JSON, models::Post, schema, };
use schema::posts::dsl::*;
use diesel::prelude::*;

#[get("/posts/published")]
pub async fn get_published() -> HttpResponse {
    load(true)
}
#[get("/posts")]
pub async fn get() -> HttpResponse {
    load(false)
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
    let results = if published_only {filtered_results.load(connection)} else {all_results.load(connection)};
    let decoded = results.expect("Error loading posts");
    //let results = expected_results.load(connection).expect("Error loading posts");
    /*
    let results = posts
        .limit(50)
        .filter(published.eq(published_only))
        .select(Post::as_select())
        .load(connection)
        .expect("Error loading posts");
    */
    HttpResponse::Ok()
        .content_type(APPLICATION_JSON)
        .json(decoded)
}

