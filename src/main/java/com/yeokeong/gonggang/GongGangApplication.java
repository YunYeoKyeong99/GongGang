package com.yeokeong.gonggang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GongGangApplication {

    public static void main(String[] args) {
        SpringApplication.run(GongGangApplication.class, args);
    }

}
