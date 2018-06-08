package io.github.yashchenkon.dockercompose.junit5.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import io.github.yashchenkon.dockercompose.junit5.extension.DockerComposeExtension;

/**
 * Entry point.
 *
 * @author Mykola Yashchenko
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DockerComposeExtension.class)
public @interface DockerCompose {
    /**
     * Docker-compose file to use. Should be located in classpath.
     *
     * @return file
     */
    String file() default "docker-compose.yml";

    /**
     * Path to logs. Library saves logs to specified path.
     *
     * @return logs path
     */
    String logs() default "";

    /**
     * Services to wait for before running the tests.
     *
     * @return services to wait for
     */
    WaitFor[] waitFor() default {};
}
