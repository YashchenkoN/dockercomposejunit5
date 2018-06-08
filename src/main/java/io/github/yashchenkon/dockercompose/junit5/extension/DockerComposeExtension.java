package io.github.yashchenkon.dockercompose.junit5.extension;

import java.io.File;
import java.util.Collections;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.configuration.DockerComposeFiles;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import io.github.yashchenkon.dockercompose.junit5.annotation.DockerCompose;
import io.github.yashchenkon.dockercompose.junit5.annotation.WaitFor;
import io.github.yashchenkon.dockercompose.junit5.constant.WaitForType;

import static com.palantir.docker.compose.DockerComposeRule.builder;

/**
 * @author Mykola Yashchenko
 */
public class DockerComposeExtension implements BeforeAllCallback, AfterAllCallback {

    private DockerComposeRule dockerComposeRule;

    public void beforeAll(final ExtensionContext extensionContext) throws Exception {
        final DockerCompose annotation = annotation(extensionContext);

        DockerComposeRule.Builder builder = builder()
                .pullOnStartup(true)
                .files(dockerCompose(annotation.file()));

        if (StringUtils.isNotEmpty(annotation.logs())) {
            builder = builder.saveLogsTo(annotation.logs());
        }

        for (final WaitFor waitFor : annotation.waitFor()) {
            builder = builder.waitingForService(waitFor.service(), WaitForType.toHealthCheck(waitFor));
        }

        dockerComposeRule = builder.build();

        dockerComposeRule.before();
    }

    public void afterAll(final ExtensionContext extensionContext) throws Exception {
        if (dockerComposeRule != null) {
            dockerComposeRule.after();
        }
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
