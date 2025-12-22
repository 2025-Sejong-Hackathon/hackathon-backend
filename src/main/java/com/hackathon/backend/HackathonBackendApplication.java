package com.hackathon.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HackathonBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackathonBackendApplication.class, args);
    }

}
