# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project state

This is a freshly generated Spring Initializr scaffold, not yet a working application. The only Java code is `ChatApplication` (the `@SpringBootApplication` entry point) and an empty `com.gencosoft.chat.agents.AgentExample` placeholder. Expect to be building features from scratch rather than fitting into existing structure.

## Build and test

```bash
./mvnw spring-boot:run                 # run the app (port 8080)
./mvnw test                            # run all tests
./mvnw test -Dtest=ChatApplicationTests # run one test class
./mvnw test -Dtest=ChatApplicationTests#contextLoads  # run one test method
./mvnw package                         # build the executable jar into target/
```

### JDK requirement

`pom.xml` sets `<java.version>25</java.version>`, so builds need JDK 25+. The shell's default `JAVA_HOME` points at JDK 21, which fails with `error: release version 25 not supported`. Prefix build commands with a 25+ JDK, e.g. `JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-26.jdk/Contents/Home ./mvnw test`. Don't report a build as broken by code changes without checking this first. IntelliJ's project SDK is set to `temurin-26` in `.idea/misc.xml`.

## Stack

- **Spring Boot 4.1.1** (`spring-boot-starter-parent`) — note the 4.x line; APIs and starter names differ from Boot 3.x. Web is `spring-boot-starter-webmvc` (not `-web`), and test slices come from separate `*-test` starters (`spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`, `spring-boot-starter-actuator-test`) rather than a single `spring-boot-starter-test`.
- **Spring AI 2.0.1**, via the `spring-ai-bom` import. The only model starter wired up is `spring-ai-starter-model-ollama`, so chat inference is expected to run against a local/self-hosted Ollama server. Keep the BOM as the version source — don't pin Spring AI artifact versions individually.
- **Spring Data JPA** is on the classpath with no datasource, driver, or entities configured yet. Adding a driver dependency and `spring.datasource.*` config is a prerequisite for the context to start once JPA is actually used.
- **Actuator** and **DevTools** (runtime-scoped, restart-on-change during `spring-boot:run`) are included.

## Configuration

`src/main/resources/application.properties` sets `spring.ai.ollama.base-url=` to an empty value. Fill this in (e.g. `http://localhost:11434`) before exercising anything that calls the model, or the Ollama client will not resolve a host.
