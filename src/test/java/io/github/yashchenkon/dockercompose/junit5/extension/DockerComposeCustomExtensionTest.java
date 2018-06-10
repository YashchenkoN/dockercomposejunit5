package io.github.yashchenkon.dockercompose.junit5.extension;

import java.io.File;
import java.io.IOException;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.Cluster;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import io.github.yashchenkon.dockercompose.junit5.annotation.DockerCompose;
import io.github.yashchenkon.dockercompose.junit5.annotation.HealthCheck;
import io.github.yashchenkon.dockercompose.junit5.annotation.WaitFor;
import io.github.yashchenkon.dockercompose.junit5.constant.HealthCheckType;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Mykola Yashchenko
 */
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
public class DockerComposeCustomExtensionTest {

    @Test
    public void verifyParameterExtension(final DockerComposeRule dockerComposeRule) {
        assertThat(dockerComposeRule, notNullValue());

        final Cluster containers = dockerComposeRule.containers();
        assertThat(containers.container("service-1")
                .portIsListeningOnHttp(
                        8080, port -> port.inFormat("http://$HOST:$EXTERNAL_PORT")
                ).succeeded(),
                equalTo(true));
        assertThat(containers.container("service-2")
                .areAllPortsOpen().succeeded(),
                equalTo(true));
    }

    @Test
    public void verifyLogs() throws IOException {
        final File logs = new File("target/logs/docker-compose");
        assertThat(logs.exists(), equalTo(true));

        final File service1Log = new File(logs, "service-1.log");
        assertThat(service1Log.exists(), equalTo(true));
        final String service1LogContent = IOUtils.toString(service1Log.toURI());
        assertThat(service1LogContent, containsString("http-server"));

        final File service2Log = new File(logs, "service-2.log");
        assertThat(service2Log.exists(), equalTo(true));
        final String service2LogContent = IOUtils.toString(service2Log.toURI());
        assertThat(service2LogContent, containsString("server started"));
    }
}
