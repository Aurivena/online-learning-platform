package dev.aurivena.lms.config;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    private final String locationsPath = "classpath:db/migration";
    @Bean
    public Flyway flyway(DataSourceProperties dataSourceProperties) {
        return Flyway.configure()
                .dataSource(dataSourceProperties.getUrl(),dataSourceProperties.getUsername(),dataSourceProperties.getPassword())
                .locations(locationsPath)
                .load();
    }
}
