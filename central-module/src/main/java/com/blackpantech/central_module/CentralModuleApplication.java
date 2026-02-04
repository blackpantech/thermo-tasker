package com.blackpantech.central_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CentralModuleApplication {
  static void main(String[] args) {
    SpringApplication.run(CentralModuleApplication.class, args);
  }
}
