use std::env;

pub fn load_key() ->  String {
    let begin = String::from("-----BEGIN PUBLIC KEY-----");
    let end = String::from("-----END PUBLIC KEY-----");

    let pub_key = env::var("PUBLIC_KEY").expect("DATABASE_URL must be set");
    let pub_key = format!("{}\n{}\n{}\n", begin, pub_key, end);
    println!("public key is:\n{}", pub_key);
    //let keycloak_auth = KeycloakAuth::default_with_pk(DecodingKey::from_rsa_pem(pub_key.as_bytes()).unwrap());
    //return keycloak_auth;
    return pub_key.clone();
}