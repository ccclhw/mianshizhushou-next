package com.yupi.mianshizhushou.aop;

import com.yupi.mianshizhushou.annotation.DistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: lhw
 * @Date: 2025-06-06 - 06 - 06 - 16:09
 * @Description: com.yupi.mianshizhushou.aop
 * @version: 1.0
 */
@Aspect
@Component
@Slf4j
public class DistributedLockAspect {
    @Resource
    private RedissonClient redissonClient;

    /*
     * 环绕通知 允许在目标方法执行前和之后都插入自定义逻辑
     * */
    @Around("@annotation(distributedLock)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = distributedLock.key();
        long leaseTime = distributedLock.leaseTime();
        long waitTime = distributedLock.waitTime();
        TimeUnit timeUnit = distributedLock.TIME_UNIT();

        //获取锁 通过Redission获取一个可重入分布式锁RLock实例
        RLock lock = redissonClient.getLock(key);
        boolean acquired = false;
        log.info("尝试加锁 key = {}", key);
        acquired = lock.tryLock(waitTime, leaseTime, timeUnit);
        try {
            if (acquired) {
                //获取锁成功 执行目标方法
                log.info("加锁成功 key = {}", key);
                return joinPoint.proceed();
            } else {
                //获取锁失败 抛出锁异常
                throw new RuntimeException("Could not acquired lock" + key);
            }
        } catch (Throwable e) {
            throw e;
        } finally {
            //判断当前线程是否持有这把分布式锁
            if (acquired && lock.isHeldByCurrentThread()) {
                // 持有锁的情况下才能释放锁
                lock.unlock();
                log.info("释放锁 key = {}", key);
            }
        }
    }
}

