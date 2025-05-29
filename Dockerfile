FROM maven as build
WORKDIR /build
COPY pom.xml .
COPY src src
RUN mvn clean install