package io.github.yashchenkon.dockercompose.junit5.extension;

import java.io.File;
import java.util.Collections;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.DockerComposeFiles;
import com.palantir.docker.compose.connection.Container;
import com.palantir.docker.compose.connection.waiting.HealthCheck;
import com.palantir.docker.compose.logging.FileLogCollector;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import io.github.yashchenkon.dockercompose.junit5.annotation.DockerCompose;
import io.github.yashchenkon.dockercompose.junit5.annotation.WaitFor;
import io.github.yashchenkon.dockercompose.junit5.constant.HealthCheckType;

import static com.palantir.docker.compose.DockerComposeRule.builder;
import static org.joda.time.Duration.standardSeconds;

/**
 * Main extension that is being executed if test class annotated with {@link DockerCompose}.
 *
 * @author Mykola Yashchenko
 */
public class DockerComposeExtension implements BeforeAllCallback, AfterAllCallback, ParameterResolver {

    private DockerComposeRule dockerComposeRule;

    public void beforeAll(final ExtensionContext extensionContext) throws Exception {
        final DockerCompose annotation = annotation(extensionContext);

        DockerComposeRule.Builder builder = builder()
                .pullOnStartup(true)
                .files(dockerCompose(annotation.file()));

        if (StringUtils.isNotEmpty(annotation.logs())) {
            final File logDir = new File(annotation.logs());
            if (logDir.exists()) {
                logDir.delete();
            }

            logDir.mkdirs();

            builder = builder.logCollector(new FileLogCollector(logDir));
        }

        for (final WaitFor waitFor : annotation.waitFor()) {
            final HealthCheck<Container> healthCheck = HealthCheckType.toHealthCheck(waitFor.healthCheck());
            if (healthCheck != null) {
                final int timeout = waitFor.healthCheck().timeout();
                builder = builder.waitingForService(waitFor.service(), healthCheck, standardSeconds(timeout));
            }
        }

        dockerComposeRule = builder.build();

        dockerComposeRule.before();
    }

    public void afterAll(final ExtensionContext extensionContext) throws Exception {
        if (dockerComposeRule != null) {
            dockerComposeRule.after();
        }
    }

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext,
                                     final ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == DockerComposeRule.class;
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext,
                                   final ExtensionContext extensionContext) throws ParameterResolutionException {
        return dockerComposeRule;
    }

    private DockerCompose annotation(final ExtensionContext extensionContext) {
        return extensionContext.getTestClass()
                .map(testClass -> testClass.getAnnotation(DockerCompose.class))
                .orElseThrow(RuntimeException::new);
    }

    private DockerComposeFiles dockerCompose(final String filename) {
        return new DockerComposeFiles(Collections.singletonList(
                new File(getClass().getClassLoader().getResource(filename).getFile())));
    }
}
