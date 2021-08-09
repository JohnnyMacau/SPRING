package com.macauslot.recruitment_ms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author jim.deng
 */
@SpringBootApplication
@ServletComponentScan
@EnableJpaAuditing
public class RecruitmenthkApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecruitmenthkApplication.class, args);
	}

}
