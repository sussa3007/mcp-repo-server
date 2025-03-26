package com.miraclestudio.mcpreposerver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableAsync
@PropertySource("classpath:/env.yml")
public class McpreposerverApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpreposerverApplication.class, args);
	}

}
