FROM maven:3.6.3-openjdk-8 as builder
COPY ./app/src/ src/
COPY ./app/pom.xml pom.xml
RUN mvn clean package -Dmaven.test.skip -Pprod

FROM java:8 as runner
COPY --from=builder target/spellbook.jar spellbook.jar
ENTRYPOINT ["java", "-jar", "/spellbook.jar"]