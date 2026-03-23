package com.foxwear.dynamicdataservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = "com.foxwear")
@EnableJpaAuditing
@ComponentScan(basePackages = {
        "com.foxwear.dynamicdataservice",
        "com.foxwear.common"
})
public class DynamicDataServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicDataServiceApplication.class, args);
    }

}
