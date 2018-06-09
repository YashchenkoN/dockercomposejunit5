package io.github.yashchenkon.dockercompose.junit5.annotation;

import io.github.yashchenkon.dockercompose.junit5.constant.HealthCheckType;

/**
 * Health check config.
 *
 * @author Mykola Yashchenko
 */
public @interface HealthCheck {

    /**
     * Wait type.
     *
     * @return wait type
     */
    HealthCheckType type();

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

    /**
     * Timeout in seconds.
     *
     * @return timeout
     */
    int timeout() default 120;
}
