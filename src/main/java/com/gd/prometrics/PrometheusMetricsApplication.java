package com.gd.prometrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EntityScan("com.gd.prometrics.model")
@ComponentScan("com.gd.prometrics")
@SpringBootApplication
public class PrometheusMetricsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrometheusMetricsApplication.class, args);
	}

}
