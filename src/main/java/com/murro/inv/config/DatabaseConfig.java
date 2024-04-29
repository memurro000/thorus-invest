package com.murro.inv.config;

import com.murro.inv.config.properties.YamlPropertySourceFactory;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource(value = "classpath:application.yaml", factory = YamlPropertySourceFactory.class)
public class DatabaseConfig {
    @Value("${data.jpa.jdbc.url}")
    private String jdbcURL;
    @Value("${data.jpa.jdbc.user}")
    private String user;
    @Value("${data.jpa.jdbc.password}")
    private String password;
    @Value("${data.jpa.jdbc.packages}")
    private String packages;
}
