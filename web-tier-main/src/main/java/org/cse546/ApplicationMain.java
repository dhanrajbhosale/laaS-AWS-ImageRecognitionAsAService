package org.cse546;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


@SpringBootApplication
public class ApplicationMain {
    private static final Logger logger= LoggerFactory.getLogger(ApplicationMain.class);
    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }
}
