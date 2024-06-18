use chrono::{DateTime, Utc};
use serde::{Deserialize, Serialize};
use actix_web::{get, HttpResponse};

use crate::app::api::constants::APPLICATION_JSON;

#[derive(Debug, Deserialize, Serialize)]
pub struct Hello {
    pub greeting: String,
    pub created_at: DateTime<Utc>,
}

impl Hello {
    pub fn new() -> Self {
        Self {
            greeting: String::from("Hello, world"),
            created_at: Utc::now()
        }
    }
}
#[get("/hello")]
pub async fn get() -> HttpResponse {
    let response = Hello::new();

    HttpResponse::Ok()
        .content_type(APPLICATION_JSON)
        .json(response)
}
