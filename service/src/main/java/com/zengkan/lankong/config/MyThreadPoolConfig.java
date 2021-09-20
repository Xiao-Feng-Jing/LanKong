package com.zengkan.lankong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/04/14/12:32
 * @Description: 线程池
 **/
@Configuration
@EnableAsync
public class MyThreadPoolConfig{

    /*
     * 默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务， 当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
     * 当队列满了，就继续创建线程，当线程数量大于等于maxPoolSize后，开始使用拒绝策略拒绝
     */
    /**IO密集型 -> 2 * N 核心线程数*/
    private static final int CORE_POOL_SIZE = 10;
    /** 最大线程数*/
    private static final int MAX_POOL_SIZE = 60;
    /** 允许空闲时间*/
    private static final int KEEP_ALIVE_SECONDS = 5;
    /** 阻塞队列容量*/
    private static final int QUEUE_CAPACITY = 20;

    @Bean("taskExecutor")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadExecutor = new ThreadPoolTaskExecutor();
        threadExecutor.setCorePoolSize(CORE_POOL_SIZE);
        threadExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        threadExecutor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
        threadExecutor.setQueueCapacity(QUEUE_CAPACITY);
        // 设置拒绝策略
        threadExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 等待所有任务结束后再关闭线程池
        threadExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadExecutor.setThreadFactory(new MyThreadFactory());
        threadExecutor.initialize();
        return threadExecutor;
    }
}
