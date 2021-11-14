package com.rest.hw.app;

import com.rest.hw.app.controller.PersonController;
import com.rest.hw.app.service.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class RestApplication {//implements CommandLineRunner {
    PersonController person;
    PersonService personService;


    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }


}



