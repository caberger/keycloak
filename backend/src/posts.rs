use actix_web::{get, HttpResponse};
use backend::establish_connection;
use crate::{constants::APPLICATION_JSON, models::Post, schema, };
use schema::posts::dsl::*;
use diesel::prelude::*;

#[get("/posts")]
pub async fn get() -> HttpResponse {
    let connection = &mut establish_connection();
    let results = posts
        .filter(published.eq(true))
        .limit(5)
        .select(Post::as_select())
        .load(connection)
        .expect("Error loading posts");
    HttpResponse::Ok()
        .content_type(APPLICATION_JSON)
        .json(results)
}
