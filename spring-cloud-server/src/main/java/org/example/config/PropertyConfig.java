package org.example.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class PropertyConfig {
    @Value("${my.property.test}")
    private String testProperty;
}
