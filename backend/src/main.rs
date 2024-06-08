use std::{env, io};

use actix_web::{middleware, App, HttpServer};

mod hello;
mod constants;
mod posts;
mod models;
mod schema;

#[actix_rt::main]
async fn main() -> io::Result<()> {
    env::set_var("RUST_LOG", "actix_web=debug,actix_server=info");
    env_logger::init();

    HttpServer::new(|| {
        App::new()
            .wrap(middleware::Logger::default()) // enable logger - always register actix-web Logger middleware last
            .service(hello::get) // register HTTP requests handlers
            .service(posts::get)
    })
    .bind("0.0.0.0:8080")?
    .run()
    .await
}