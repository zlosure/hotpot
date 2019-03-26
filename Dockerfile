FROM clojure:alpine

WORKDIR /hotpot

EXPOSE 55555

ADD . .

ENTRYPOINT ["bin/entry-point"]
