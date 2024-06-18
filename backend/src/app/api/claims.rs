use actix_web_middleware_keycloak_auth::{KeycloakRoles, Role};
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

pub fn is_user_in_role(roles: KeycloakRoles, role: &str) -> bool {
    let found = roles.to_vec().into_iter().find(|r| match r {
        Role::Realm {role: r } => r == role,  
        _ => false
    });

    let ret = match found {
        Some(_) => true,
        None => false
    };

    ret
}