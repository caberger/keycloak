use std::{env, io, process};
use port_scanner::scan_port;

use actix_web::{middleware, App, HttpServer};

mod hello;
mod constants;
mod posts;
mod models;
mod schema;

async fn serve() -> io::Result<()> {
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
#[actix_rt::main]
async fn main() -> io::Result<()> {
    let args: Vec<String> = env::args().collect();

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
    serve()
    .await

}
