package com.epamlearning.configs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(ApplicationConfig.class)
@ContextConfiguration(classes = ApplicationConfig.class)
public class ApplicationConfigTest {

    @Value("${data.file.path}")
    private String yourProperty;

    @Test
    public void testApplicationContext() {
        // The purpose of this test is to ensure that the application context loads successfully.
        // If the context is loaded without errors, this test will pass.
    }

    @Test
    public void testYourProperty() {
        assertNotNull(yourProperty, "Property should not be null. Check your context.properties file.");
    }
}
