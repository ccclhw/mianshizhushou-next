package com.yupi.mianshizhushou.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: lhw
 * @Date: 2025-06-06 - 06 - 06 - 15:59
 * @Description: com.yupi.mianshizhushou.annotation
 * @version: 1.0
 * 自定义分布式锁注解 防止重复执行定时任务
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)//编译进.class 运行时可用 可通过反射读取注解
public @interface DistributedLock {
    /*
     * 锁的名称
     * */
    String key();

    /*
     * 持锁时间
     * */
    long leaseTime() default 30000;

    /*
     * 等待时间
     * */
    long waitTime() default 10000;

    /*
     * 时间单位默认为毫秒
     * */
    TimeUnit TIME_UNIT() default TimeUnit.MICROSECONDS;

}
