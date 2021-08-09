package com.macauslot.recruitmentadmin;

import com.macauslot.recruitmentadmin.repository.BaseRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan
@EnableJpaAuditing
@EnableScheduling
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class RecruitmentadminApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecruitmentadminApplication.class, args);
    }

}
