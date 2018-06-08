package io.github.yashchenkon.dockercompose.junit5.constant;

import com.palantir.docker.compose.connection.Container;
import com.palantir.docker.compose.connection.waiting.HealthCheck;
import com.palantir.docker.compose.connection.waiting.HealthChecks;

import io.github.yashchenkon.dockercompose.junit5.annotation.WaitFor;

/**
 * Type of verifying that service is up.
 *
 * @author Mykola Yashchenko
 */
public enum WaitForType {
    ALL_PORTS_OPEN,
    TO_RESPOND_OVER_HTTP;

    /**
     * Detects which healthcheck should be performed against particular service.
     *
     * @param waitFor - config
     * @return healthcheck strategy
     */
    public static HealthCheck<Container> toHealthCheck(final WaitFor waitFor) {
        switch (waitFor.type()) {
            case ALL_PORTS_OPEN:
                return HealthChecks.toHaveAllPortsOpen();
            case TO_RESPOND_OVER_HTTP:
                return HealthChecks.toRespondOverHttp(waitFor.port(), port -> port.inFormat(waitFor.url()));
        }

        return null;
    }
}
