package io.github.yashchenkon.dockercompose.junit5.annotation;

import io.github.yashchenkon.dockercompose.junit5.constant.WaitForType;

/**
 * @author Mykola Yashchenko
 */
public @interface WaitFor {
    String service();
    WaitForType type();

    int port() default 0;
    String url() default "";
}
