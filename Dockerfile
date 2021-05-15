FROM openjdk:15-jdk-oraclelinux8
WORKDIR /tmp
COPY ./pom.xml /tmp/pom.xml
COPY ./target/classes/ /tmp
RUN mvn -B -f /tmp/pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
ENTRYPOINT ["java","TrafficNode.TrafficNodeFactory"]