package com.kefas.diaryblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

import javax.persistence.Entity;

@SpringBootApplication
@EntityScan(basePackageClasses = {DiaryBlogApplication.class, Jsr310Converters.class})
public class DiaryBlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiaryBlogApplication.class, args);
	}

}
