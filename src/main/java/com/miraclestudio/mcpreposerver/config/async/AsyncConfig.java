package com.miraclestudio.mcpreposerver.config.async;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 레포지토리 리뷰 전용 스레드풀 설정
     */
    @Bean(name = "reviewTaskExecutor")
    public Executor reviewTaskExecutor() {
        log.debug("레포지토리 리뷰를 위한 스레드풀 생성");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("MCP-Review-");
        executor.setRejectedExecutionHandler((r, e) -> {
            log.error("작업이 거부되었습니다. 대기열이 가득 찼습니다.");
            throw new RejectedExecutionException("작업이 거부되었습니다. 나중에 다시 시도해주세요.");
        });
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(600); // 리뷰 작업은 시간이 오래 걸릴 수 있어 타임아웃 설정 길게
        executor.initialize();
        return executor;
    }

    /**
     * 비동기 작업 중 발생한 예외 핸들러
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}