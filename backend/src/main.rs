use std::{env, io, process};
use port_scanner::scan_port;
use dotenvy::dotenv;
use actix_web::{middleware, web, App, HttpServer};
use pubkey::load_key;
use actix_web_middleware_keycloak_auth::{AlwaysReturnPolicy, DecodingKey, KeycloakAuth, Role};

const PORT: i32 = 8080;

mod hello;
mod constants;
mod posts;
mod models;
mod schema;
mod pubkey;

async fn serve() -> io::Result<()> {
    env::set_var("RUST_LOG", "actix_web=debug,actix_server=info");
    env_logger::init();
    let bind_addr = std::format!("0.0.0.0:{}", PORT);
    let pub_key = load_key();

    HttpServer::new(move || {
        let auth = KeycloakAuth::default_with_pk(DecodingKey::from_rsa_pem(pub_key.as_bytes()).unwrap());
        let keycloak_editor_auth = KeycloakAuth {
            detailed_responses: true,
            passthrough_policy: AlwaysReturnPolicy,
            keycloak_oid_public_key: DecodingKey::from_rsa_pem(pub_key.as_bytes()).unwrap(),
            required_roles: vec![
                Role::Realm { role: "editor".to_owned() }, // The "admin" realm role must be provided in the JWT
            ],
        };
        App::new()
            .wrap(middleware::Logger::default()) // enable logger - always register actix-web Logger middleware last
            .service(web::scope("/public")
                .service(hello::get)
            )
            .service(web::scope("/api")
                .service(posts::get_published)   
                .wrap(auth)
                .service(posts::get)
                .wrap(keycloak_editor_auth)
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
