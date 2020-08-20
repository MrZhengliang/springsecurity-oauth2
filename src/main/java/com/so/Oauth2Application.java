package com.so;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

/**
 * 认证服务启动类
 * @author fu
 *
 */
@SpringBootApplication
@EnableAuthorizationServer
public class Oauth2Application {
	public static void main(String[] args) {
		SpringApplication.run(Oauth2Application.class, args);
	}
}
