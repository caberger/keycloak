FROM rust as builder

RUN mkdir -p /usr/local/src
COPY ./ usr/local/src/
RUN ls -l /usr/local/src/ && \
    cd /usr/local/src/ && \
        cargo build --release && \
    cd

FROM rust:slim
RUN apt update && apt install -y postgresql-client && apt clean
COPY --from=builder /usr/local/src/target/release/backend /backend
CMD [ "/backend" ]
