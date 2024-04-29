package com.murro.inv.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.murro.inv")
@EnableJpaRepositories("com.murro.inv")
@Import(OrmConfig.class)
public class RepoConfig {


}
