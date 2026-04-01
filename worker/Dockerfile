FROM bellsoft/liberica-openjre-debian:17

WORKDIR /usr/src/app

COPY target/image-processor-0.0.1-SNAPSHOT.jar ./

ENTRYPOINT ["java", "-jar", "image-processor-0.0.1-SNAPSHOT.jar"]

