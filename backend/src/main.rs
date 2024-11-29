use std::{env, io, process};
use actix_web::{middleware, web, App, HttpServer};
use actix_web_middleware_keycloak_auth::{DecodingKey, KeycloakAuth};

use app::{api::{hello, postsapi}, pubkey::load_key};


use port_scanner::scan_port;
use dotenvy::dotenv;

const PORT: i32 = 8080;

pub mod app;
mod models;
mod schema;

async fn serve() -> io::Result<()> {
    env::set_var("RUST_LOG", "actix_web=debug,actix_server=info");
    env_logger::init();
    let bind_addr = std::format!("0.0.0.0:{}", PORT);
    let pub_key = load_key();

    HttpServer::new(move || {
        let auth = KeycloakAuth::default_with_pk(DecodingKey::from_rsa_pem(pub_key.as_bytes()).unwrap());
        App::new()
            .wrap(middleware::Logger::default()) // enable logger - always register actix-web Logger middleware last
            .service(web::scope("/api")
                .service(hello::get)
            )
            .service(web::scope("/api")
                .service(postsapi::get)
                .wrap(auth)
            )
            
    })
    .bind(bind_addr)?
    .run()
    .await
}

#[actix_rt::main]
async fn main() -> io::Result<()> {
    let args: Vec<String> = env::args().collect();
    dotenv().ok();

    if args.len() > 1  {
        let option = &args[1];
        let mut retcode = 0;
        match option == "--health" {
            true => {
                let avail = scan_port(8080);
                if !avail {
                    println!("health check failed");
                    retcode = 1;
                } else {
                    println!("health OK");
                }
            },
            _ => {
                println!("unknown option {}", option);
                retcode = 2;
            }
        }
        process::exit(retcode)
    }

    serve().await
}
