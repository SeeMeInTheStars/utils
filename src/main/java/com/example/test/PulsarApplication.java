package com.example.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class PulsarApplication {
    public static void main(String[] args) {
        SpringApplication.run(PulsarApplication.class, args);

    }

    private static String localDateToString(LocalDate localDate) {
        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        // 格式化LocalDate对象
        return localDate.format(formatter);
    }
}
