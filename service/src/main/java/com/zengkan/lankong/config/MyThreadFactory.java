package com.zengkan.lankong.config;

import javax.annotation.Nonnull;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/04/14/12:37
 * @Description: 线程工厂
 **/
public class MyThreadFactory implements ThreadFactory {

    private final AtomicInteger threadCount = new AtomicInteger(1);
    private final String namePrefix;

    public MyThreadFactory(){
        namePrefix = "file-pool-thread-";
    }

    @Override
    public Thread newThread(@Nonnull Runnable runnable) {
        Thread thread = new Thread(runnable,namePrefix + threadCount.getAndIncrement());
        if (thread.isDaemon()){
            //守护进程设为false
            thread.setDaemon(false);
        }
        if (thread.getPriority() != Thread.NORM_PRIORITY){
            //设置优先级
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
