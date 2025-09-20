package com.yzr.dida;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.yzr.dida.mappers")
public class DidaApplication {
    public static void main(String[] args) {
        SpringApplication.run(DidaApplication.class, args);
    }

}
