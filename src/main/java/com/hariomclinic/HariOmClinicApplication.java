package com.hariomclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * HariOmClinicApplication - Entry point of the Spring Boot application.
 *
 * @SpringBootApplication is a convenience annotation that combines:
 *   - @Configuration       : marks this class as a source of bean definitions
 *   - @EnableAutoConfiguration : tells Spring Boot to auto-configure based on classpath
 *   - @ComponentScan       : scans all sub-packages for Spring components
 *
 * SpringApplication.run() bootstraps the application, starts embedded Tomcat server on port 8080.
 */
@SpringBootApplication
public class HariOmClinicApplication {
    public static void main(String[] args) {
        SpringApplication.run(HariOmClinicApplication.class, args);
    }
}
