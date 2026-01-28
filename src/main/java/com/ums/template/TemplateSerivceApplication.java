package com.ums.template;

import com.ums.template.config.TestProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(TestProperties.class)
public class TemplateSerivceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TemplateSerivceApplication.class, args);
    }

}
