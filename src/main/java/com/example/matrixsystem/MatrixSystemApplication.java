package com.example.matrixsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MatrixSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatrixSystemApplication.class, args);
    }

}
