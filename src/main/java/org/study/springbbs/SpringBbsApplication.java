package org.study.springbbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringBbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBbsApplication.class, args);
    }

}
