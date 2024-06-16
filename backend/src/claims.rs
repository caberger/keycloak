use serde::Deserialize;
/**
 *  the keycloak json web token claims that we need, injected into our resources.
 */
#[derive(Debug, Deserialize)]
pub struct RealmAccess {
    pub roles: Vec<String>
}
#[derive(Debug, Deserialize)]
pub struct ClaimsFromJwt {
    pub preferred_username: String,
    pub realm_access: RealmAccess
}

pub static EDITOR_ROLE:&str = "editor";