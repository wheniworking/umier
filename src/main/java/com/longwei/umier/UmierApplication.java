package com.longwei.umier;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.longwei.umier.dao")
public class UmierApplication {

	public static void main(String[] args) {
		SpringApplication.run(UmierApplication.class, args);
	}
}
