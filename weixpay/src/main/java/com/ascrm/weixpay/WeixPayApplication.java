package com.ascrm.weixpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//开启通用注解扫描  
@ComponentScan("com.ascrm")
public class WeixPayApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeixPayApplication.class, args);
	}
}
