FROM rust

RUN apt update
RUN apt-get -y install musl-dev

RUN mkdir -p /usr/local/src
COPY ./ usr/local/src/
RUN ls -l /usr/local/src/ && \
    cd /usr/local/src/ && \
        cargo build --release && \
    cd

CMD [ "tail", "-f", "/dev/null" ]