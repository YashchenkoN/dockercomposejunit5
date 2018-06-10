## Docker-compose JUnit5
[![Build Status](https://travis-ci.org/YashchenkoN/dockercomposejunit5.svg?branch=master)](https://travis-ci.org/YashchenkoN/dockercomposejunit5)

Library allows developers to run docker-compose from Java JUnit5 tests.
Inspired by [docker-compose-rule](https://github.com/palantir/docker-compose-rule)

This library allows:

- start containers defined in docker-compose.yml before JUnit5 tests and shutdown them afterwards
- write docker-compose logs to separate folder
- wait until services become available

### Usage
Annotate your test class with `@DockerCompose` annotation:

```java
@DockerCompose
public class Test1 {

    @Test
    public void shouldDoSomething() {
    }
}
```

By default, extension looks for `docker-compose.yml` in the classpath root, doesn't writes logs and doesn't wait until
services are available.

To manage all this stuff, specify appropriate attributes of `@DockerCompose` annotation.

```java
@DockerCompose(
        file = "docker-compose-custom.yml",
        logs = "target/logs/docker-compose",
        waitFor = {
                @WaitFor(
                        service = "service-1",
                        healthCheck = @HealthCheck(
                                type = HealthCheckType.TO_RESPOND_OVER_HTTP,
                                port = 8080,
                                timeout = 60
                        )
                ),
                @WaitFor(
                        service = "service-2",
                        healthCheck = @HealthCheck(type = HealthCheckType.ALL_PORTS_OPEN)
                )
        }
)
public class Test2 {
    
    @Test
    public void verify() {
    }
}
```

In this case, it'll use `docker-compose-custom.yml` from the classpath, write logs to
`target/logs/docker-compose` dir and wait until `service-1` and `service-2` become available.

### Accessing a service

This extension implements `ParameterResolver` interface, so you can pass `DockerComposeRule` object
as an argument of your test:

```java
@DockerCompose
public class DockerComposeCustomExtensionTest {

    @Test
    public void verifyParameterExtension(final DockerComposeRule dockerComposeRule) {
        
    }
}
```

For additional details of `DockerComposeRule`, please go to [docker-compose-rule](https://github.com/palantir/docker-compose-rule)

### Installation

Add following dependency to `pom.xml`:

```xml
<dependency>
    <groupId>io.github.yashchenkon</groupId>
    <artifactId>dockercomposejunit5</artifactId>
    <version>1.0.0</version>
</dependency>
```