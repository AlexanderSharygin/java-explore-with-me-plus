package ru.practicum.ewm.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"ru.practicum.ewm.client",
        "ru.practicum.ewm.main.category",
        "ru.practicum.ewm.main.event",
        "ru.practicum.ewm.main.category",
        "ru.practicum.ewm.main.exception",
        "ru.practicum.ewm.main.request",
        "ru.practicum.ewm.main.user"})
public class MainService {
    public static void main(String[] args) {
        SpringApplication.run(MainService.class, args);
    }
}