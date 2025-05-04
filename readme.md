# Securing a Web-App using Keycloak

This example shows how to secure a simple Web-Application using a REST - API that is protected with [Json Web Tokens](https://jwt.io/) using [keycloak](https://www.keycloak.org/).


## Frontend

The frontend uses lit-html to show a simple main page that displays user details and user roles from the keycloak authentication service and a greeting fetched from the backend that is secured by json web tokens received from keycloak

## Alternatives

The alternatives/rust folder is a simple ORM Rest - Api implemented in rust (deprecated).
The vintage-frontend folde is a angular application showing how to use keycloak (deprecated),

## backend

backend contains an application-server implemented with quarkus. 

## Authentication and Authorization server

The keycloak server implements the user database and returns Json Web Tokens on successful authentication. As a backend a postgresql database is used.

## Database

The "keycloak" database is the storage for keycloak.

We use [eclipse store](https://eclipsestore.io/) as database for the backend and support backup as well as export/import to xlsx (Libreoffice Calc and/or Excel format). 
For motivation see [Storage](https://docs.eclipsestore.io/manual/storage/index.html)

## Building

Building is supported on Linux and OSX.

You the follwing build tools:
- [nodejs](https://nodejs.org/)
- maven

To build the docker images run the following:
```bash
./start.sh
```


to start the system without full compile change to the compose folder and run the following:
```bash
cd compose
./start.sh
```
The services have health checks included, so dependent services only start when the services they need are healthy. Wait until all four services are healthy.

## Adding users to keycloak

to create the demo realm import the file ./components/keycloak/setup/realm-export.json in the keycloak admin panel. You can find the keycloak username and password in the docker-compose.yaml file.

## delete all docker containers, images and volumes

to clear the cache and clean all images you can use ```./compose/clean-docker.sh``` before the build.

## backup the database

```bash
docker compose exec postgres -- pg_dump -U keycloak
```

## Development using a local docker compose database and keycloak image

# OIDC Security Concepts

When designing a token-based architecture, it's important to understand how identity data is passed around in the system. Claims provide a fundamental means for how to trust that the data is valid and true.
see [Claims Explained](https://curity.io/resources/learn/what-are-claims-and-how-they-are-used/)

- Consent
- Claim
- Asserting Party
- Scope
- Permission
- Token
- Priviledge

## 1 Consent

Consent is the act of letting the user participate in deciding what data to share with a third party.

## 2 Claim

A claim is statement that a particular entity has a particular property.
The claim is asserted by some entity - the asserting party. The asserting party
states that the subject has some attribute. Claims are mapped to scopes (scope of access).

### 2.1 Values
On the technical level, the value in a claim can be anything - a string, a dictionary, a list, a simple boolean or anything else. Of course if you want to use something very unusual, you need to make sure that it can be managed by any application that needs to handle it.

### 2.2 Asking for Consent

When a user gives consent, the user is consenting to the release of user related data.

In the context of claims architecture, these data are the claims that will be issued to the client. Since claims can contain sensitive information such as an email address or account number, it is important to let the user know what is being shared with a third party.

## 3 Asserting Party

When a claim is issued, there is an assertion that the value associated with the claim name is true. The entity providing the assertion is called an asserting party. However, the claim itself has something called an authority--the place where it was asserted. Normally the asserting party and the authority are the same, but sometimes they are not.

## 4 Scope

A scope is simply a grouping of claims.

Although the name sometimes lends itself to other interpretations. You can think of claims as access ranges or "scopes of access". Depending on whether you use OAuth or OpenID Connect, scopes are are to a greater or lesser degree defined.

OAuth defines scopes in a very open and forgiving way. In OAuth, a scope is a string that may represent a resource the Client requests access to.

In OpenID Connect, however, a scope is defined in a more clear-cut manner, and the specification even defines pre-defined scopes that have meaning in the OpenID Connect context.

Claims are not part of OAuth but are added in OpenID Connect. Therefore, it is only the OpenID Connect specification that maps pre-defined claims to pre-defined scopes.

Example: The ***email*** scope contains the claims ***email*** and ***email_verified***

## 5. Token

See [jwt.io](https://jwt.io/) for the anatomy of a token.

## 6. Permission

Usually, you have a clear understanding of what a permission is. Commonly, you say that you have the permission to drive your car, load stuff in its trunk compartment, inspect its hood, clean it, sell it, and so on.
These common activities make you think that a permission is something related to you, the user. Actually, in the computer security context, a permission is something related to an object, to a resource, not to the user of that resource.
**A permission is a declaration of an action that can be executed on a resource**. It describes intrinsic properties of resources, which exist independently of the user. In the scenarios depicted above, driving and filling the trunk are actions you can do with your car: they are permissions attached to your car. The same happens with the action of accessing your computer, reading your emails, and so on. They all are permissions on those resources.
Well, that looks weird and a bit pedantic, even philosophical in some ways. But it is fundamental to accurately define this concept to understand the rest.

## Priviledge

f permissions are bound to the resource, how can you express the ability to perform an action on that resource? How can you express the fact that someone is authorized to drive their car? What you need are privileges.

Simply put, privileges are assigned permissions. When you assign a permission to a user, you are granting them a privilege. If you assign a user the permission to read a document, you are granting them the privilege to read that document.Usually, permissions and privileges are used interchangeably, but technically they have precise and different meanings in the context of computer security.

**So, resources expose permissions. Users have privileges**

## Scopes and claims in use

Scopes enable a mechanism to define what an application can do on behalf of the user. 
When used in the authorization flows, the client sends an authorize request, including the scope or scopes or specific claims it needs (and ideally, only those it needs) to the OpenID Connect provider. This eventually results in the issuance of one or many tokens, which contains claims according to the requested scopes.

Actually, it is not the authorization server that allows the application to exercise the user's privileges. The final word on granting delegated access to the application is the user's own. The user can approve or reject delegated access to their resource with specific scopes by using a consent screen

### 1 Receiving Scopes
When users provide consent, they may not be intending to grant consent to everything, and the application may request scopes that the user has not consented to.

In such cases, the server overrides the scope request and does not provide what was asked for. However, the server must still include the scope in its response, informing the client what scope was actually issued.

Therefore, it is important that the application requesting a scope (or claim) has a process in place for how to respond when what was requested is not what was received. This may include asking for additional consent, informing the user about reduced functionality, or perhaps requiring a complete re-authorization.
---
## Implementation

### notes
e-paper: see waveshare e-paper on amazon
Images sind auf office-server Projekte/key.fit/manual
