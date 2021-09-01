package br.com.zupacademy.propostas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
@EnableWebSecurity
public class PropostasApplication {
    public static void main(String[] args) {
        SpringApplication.run(PropostasApplication.class, args);
    }
}