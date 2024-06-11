# Rust REST-API Backend

## Building the backend docker image

We do not want to use rust:slim, because it is much to big. We want to link the backend statically and use a "FROM scratch" docker,
where the only command ist the backend binary.

So we build in two steps: 
1. We build a builder image from scratch. In order to be independent of distributions we download and compile postgresql statically.
2. Create a docker image from scratch with the static executable (musl lib must be linked dynamically, so this is also copied).

This results in an image size of less then 12MB for our application server. Perfectly small for the cloud.
