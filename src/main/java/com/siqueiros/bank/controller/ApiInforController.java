package com.siqueiros.bank.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/greeting")
public class GreetingController {
    @GetMapping()
    public List<String> getGreeting() {

        return "Welcome to my Bank API" +
                "\nVersion: 1.0.0" +
                "\nAuthor: Pedro Siqueiros" +
                "\nTechnologies: " +
                "\n\tSpring Boot" +
                "\n\tPostgreSQL" +
                "\n\tJava SE 21";
    }
}
