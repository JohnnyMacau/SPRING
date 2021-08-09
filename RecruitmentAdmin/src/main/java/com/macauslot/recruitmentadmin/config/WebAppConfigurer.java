package com.macauslot.recruitmentadmin.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 主配置类
 *
 * @author jim.deng
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Bean
    public TaskExecutor getTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 最大线程数10：线程池最大的线程数，只有在缓冲队列满了之后才会申请超过核心线程数的线程
        taskExecutor.setMaxPoolSize(10);
        // 核心线程数5：线程池创建时候初始化的线程数
        taskExecutor.setCorePoolSize(5);
        // 缓冲队列20：用来缓冲执行任务的队列
        taskExecutor.setQueueCapacity(20);
        taskExecutor.setThreadNamePrefix("EmailAsync-");

        return taskExecutor;
    }

}
