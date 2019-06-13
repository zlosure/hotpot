FROM clojure:alpine

# https://stackoverflow.com/a/55568352/11438506
RUN apk --no-cache --update add libc6-compat
RUN ln -s /lib/libc.musl-x86_64.so.1 /lib/ld-linux-x86-64.so.2

WORKDIR /hotpot

EXPOSE 55555

ADD . .

ENTRYPOINT ["bin/entry-point"]
