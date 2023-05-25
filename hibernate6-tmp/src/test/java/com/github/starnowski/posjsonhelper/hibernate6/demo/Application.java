package com.github.starnowski.posjsonhelper.hibernate6.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public static final String CLEAR_DATABASE_SCRIPT_PATH = "clean-database.sql";
    public static final String ITEMS_SCRIPT_PATH = "items.sql";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
