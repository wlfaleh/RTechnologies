package com.rt.fr.RTechnologies;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RTechnologiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RTechnologiesApplication.class, args);
    }

    @RestController
    class InfoController {
        @Value("${demo.message}")
        private String message;

        @GetMapping("/api/info")
        public String getInfo() {
            return "Message: " + message;
        }
    }
}