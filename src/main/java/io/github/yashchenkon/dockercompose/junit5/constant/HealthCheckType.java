package io.github.yashchenkon.dockercompose.junit5.constant;

import com.palantir.docker.compose.connection.Container;
import com.palantir.docker.compose.connection.waiting.HealthCheck;
import com.palantir.docker.compose.connection.waiting.HealthChecks;

/**
 * Type of verifying that service is up.
 *
 * @author Mykola Yashchenko
 */
public enum HealthCheckType {
    ALL_PORTS_OPEN,
    TO_RESPOND_OVER_HTTP,
    TO_RESPOND_2XX_OVER_HTTP,
    IGNORE;

    /**
     * Detects which healthcheck should be performed against particular service.
     *
     * @param healthCheck - config
     * @return healthcheck strategy
     */
    public static HealthCheck<Container> toHealthCheck(final io.github.yashchenkon.dockercompose.junit5.annotation.HealthCheck healthCheck) {
        switch (healthCheck.type()) {
            case ALL_PORTS_OPEN:
                return HealthChecks.toHaveAllPortsOpen();
            case TO_RESPOND_OVER_HTTP:
                return HealthChecks.toRespondOverHttp(healthCheck.port(), port -> port.inFormat(healthCheck.url()));
            case TO_RESPOND_2XX_OVER_HTTP:
                return HealthChecks.toRespond2xxOverHttp(healthCheck.port(), port -> port.inFormat(healthCheck.url()));
        }

        return null;
    }
}
