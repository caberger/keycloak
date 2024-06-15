FROM builder as cargo-builder

RUN mkdir -p /usr/local/src/backend
COPY ./ /usr/local/src/backend
RUN \
    cd /usr/local/src/backend && \
        source $HOME/.cargo/env && \
        RUSTFLAGS='-C target-feature=+crt-static -C link-self-contained=yes -lpq -lpgcommon -lpgtypes -lecpg -lpgcommon_shlib -lpgport -lpgfeutils' cargo build --release --target x86_64-unknown-linux-musl && \
        ls -l /usr/local/src/backend/target/x86_64-unknown-linux-musl/release/ && \
    cd

FROM scratch
COPY --from=cargo-builder /usr/local/src/backend/target/x86_64-unknown-linux-musl/release/backend /backend
COPY --from=cargo-builder /lib/ld-musl-x86_64.so.1 /lib
HEALTHCHECK --interval=60s --timeout=3s --start-period=5s --start-interval=1s --retries=3 CMD [ "/backend", "--health" ]
CMD [ "/backend" ]
