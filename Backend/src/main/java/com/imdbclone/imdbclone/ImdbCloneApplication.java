package com.imdbclone.imdbclone;

import com.imdbclone.imdbclone.component.KeycloakClient;
import com.imdbclone.imdbclone.component.RapidApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableConfigurationProperties({RapidApiProperties.class, KeycloakClient.class})
@EnableAsync
@EnableJpaAuditing
public class ImdbCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImdbCloneApplication.class, args);
    }

}
