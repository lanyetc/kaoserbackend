FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD demo-0.0.1-SNAPSHOT.jar.jar app.jar
ENTRYPOINT "java","-jar","/app.jar"]