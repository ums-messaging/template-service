package com.ums.template.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@RefreshScope
@ConfigurationProperties(prefix = "myapp.mail")
@Getter
@AllArgsConstructor
@ToString
public class TestProperties {
    private final String host;
    private final int port;
    private final String username;
    private final String password;
}