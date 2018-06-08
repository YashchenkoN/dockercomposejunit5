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
    String file() default "docker-compose.yml";

    String logs() default "";

    WaitFor[] waitFor() default {};
}
