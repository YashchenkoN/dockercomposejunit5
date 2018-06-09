package io.github.yashchenkon.dockercompose.junit5.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.yashchenkon.dockercompose.junit5.constant.HealthCheckType;

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
     * Health check operation to perform.
     *
     * @return health check
     */
    HealthCheck healthCheck() default @HealthCheck(type = HealthCheckType.IGNORE);
}
