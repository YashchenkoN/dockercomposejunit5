package io.github.yashchenkon.dockercompose.junit5.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.yashchenkon.dockercompose.junit5.constant.WaitForType;

/**
 * Annotation describes the service to wait before executing the tests.
 *
 * @author Mykola Yashchenko
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WaitFor {
    /**
     * Service name from docker-compose.yml
     *
     * @return service name
     */
    String service();

    /**
     * Wait type.
     *
     * @return wait type
     */
    WaitForType type();

    /**
     * Port to wait.
     *
     * @return port
     */
    int port() default 0;

    /**
     * URL to check service availability.
     *
     * @return url
     */
    String url() default "";
}
