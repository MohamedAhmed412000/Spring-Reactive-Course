package com.project.webflux;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "logging.level.org.springframework.r2dbc=debug")
public abstract class AbstractTest {
}
