FROM alpine

RUN apk update
RUN apk add curl musl-dev postgresql-dev icu-data-full
RUN apk add zlib-dev bison flex readline-dev make perl linux-headers gcc g++ icu-dev

WORKDIR /root
RUN curl --proto '=https' --tlsv1.2 -sSf https://sh.rustup.rs -o $HOME/rustup.sh && chmod +x $HOME/rustup.sh && sh $HOME/rustup.sh -y -v
ENV PATH="/root/.cargo/bin:${PATH}"
RUN source .cargo/env && cargo --version

RUN mkdir -p /usr/local/src
RUN \
    cd /usr/local/src/ && \
        curl -L https://github.com/postgres/postgres/archive/refs/tags/REL_16_3.tar.gz -o postgres.tgz && \
        tar -xzf postgres.tgz && mv postgres-R* postgres && \
        cd postgres && ./configure --prefix=/usr/local && make install && cd .. && \
    cd

#CMD [ "tail", "-f", "/dev/null"]