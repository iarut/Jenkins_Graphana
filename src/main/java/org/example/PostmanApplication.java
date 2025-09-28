package org.example;

import org.example.model.Cat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PostmanApplication implements CommandLineRunner {
    @Autowired
    Cat cat;
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Cat age = " + cat.getAge());
    }

    public static void main(String[] args) {
        SpringApplication.run(PostmanApplication.class, args);
    }

}
