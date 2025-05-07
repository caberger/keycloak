use std::env;

use diesel::pg::PgConnection;
use diesel::r2d2::{ConnectionManager, Pool, PooledConnection};
use once_cell::sync::Lazy;

use crate::app::errors::AppError;

pub type DbPool = Pool<ConnectionManager<PgConnection>>;
pub type DbPooledConnection = PooledConnection<ConnectionManager<PgConnection>>;

pub static DB: Lazy<DbPool> = Lazy::new(|| {
    let database_url = env::var("DATABASE_URL").expect("DATABASE_URL must be set");

    println!("Connecting to database {}...", database_url);
    let manager = ConnectionManager::<PgConnection>::new(database_url);

    Pool::builder()
        .build(manager)
        .expect("Failed to create database connection pool.")
});
/** get a pooled database connection */
pub fn get_connection() -> Result<DbPooledConnection, AppError> {
    DB.get().map_err(|e| {
        AppError::new(500)
            .cause(e)
            .message("Failed to load database")
    })
}