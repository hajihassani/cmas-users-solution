package com.cmas.solution;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CmasUsersSolutionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmasUsersSolutionApplication.class, args);
        log.info("\n-------------------------\n| Users app is running! |\n-------------------------\n");
    }

}
