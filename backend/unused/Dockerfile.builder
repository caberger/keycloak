FROM rust:alpine

#RUN apt update
#RUN apt-get -y install musl-dev libpq-dev postgresql python3
RUN apk add musl-dev postgresql-dev postgresql

RUN mkdir -p /usr/local/src
COPY ./ usr/local/src/
RUN ls -l /usr/local/src/ && \
    cd /usr/local/src/ && \
        #RUSTFLAGS='-C target-feature=+crt-static -lpq' cargo build --release --target=x86_64-unknown-linux-musl && \
    cd

CMD [ "tail", "-f", "/dev/null" ]