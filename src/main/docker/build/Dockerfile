FROM maven:3-jdk-8-alpine
RUN sed -i -e 's/v[[:digit:]]\.[[:digit:]]/edge/g' /etc/apk/repositories
RUN apk update && apk add --update nodejs nodejs-npm

