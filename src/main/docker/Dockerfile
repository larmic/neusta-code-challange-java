FROM eclipse-temurin:17

MAINTAINER Lars Michaelis <mail@larmic.de>

VOLUME /tmp
ADD ncc.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]