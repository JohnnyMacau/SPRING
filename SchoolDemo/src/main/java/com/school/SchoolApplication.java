package com.school;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.school.repository.BaseRepositoryImpl;

@SpringBootApplication
//@ServletComponentScan
//@EnableJpaAuditing
//@EnableScheduling
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class SchoolApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolApplication.class, args);
	}

}
