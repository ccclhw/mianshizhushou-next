package com.yupi.mianshizhushou.manager;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 通用计数器
 */
@Slf4j
@Component
public class CounterManager {

    @Resource
    private RedissonClient redissonClient;


    /**
     * 增加并返回计数，默认统计一分钟内的计数结果
     *
     * @param key 缓存键
     * @return
     */
    public long increment(String key) {
        return increment(key, 1, TimeUnit.SECONDS, 60);
    }

    /**
     * 自增计数器
     *
     * @param key          缓存键
     * @param timeInterval 时间间隔
     * @param timeUnit     时间单位
     * @return
     */
    public long increment(String key, int timeInterval, TimeUnit timeUnit) {
        int expirationTimeInSeconds;
        switch (timeUnit) {
            case SECONDS:
                expirationTimeInSeconds = timeInterval;
                break;
            case MINUTES:
                expirationTimeInSeconds = timeInterval * 60;
                break;
            case HOURS:
                expirationTimeInSeconds = timeInterval * 60 * 60;
                break;
            case DAYS:
                expirationTimeInSeconds = timeInterval * 60 * 60 * 24;
                break;
            default:
                throw new IllegalArgumentException("Invalid time unit");
        }
        return increment(key, timeInterval, timeUnit, expirationTimeInSeconds);
    }

    /**
     * 获取计数器
     *
     * @param key          缓存键
     * @param timeInterval 间隔时间
     * @param timeUnit     时间单位
     * @param expireTime   过期时间
     * @return
     */
    public long increment(String key, int timeInterval, TimeUnit timeUnit, int expireTime) {
        if (StrUtil.isEmpty(key)) {
            return 0;
        }
        //根据时间粒度生成key
        long timeFactor = 0;
        //getEpochSecond()获取当前秒级时间戳
        long currentEpochSecond = Instant.now().getEpochSecond();
        switch (timeUnit) {
            case SECONDS:
                timeFactor = currentEpochSecond / timeInterval;
                break;
            case MINUTES:
                timeFactor = currentEpochSecond / 60 / timeInterval;
                break;
            case HOURS:
                timeFactor = currentEpochSecond / 60 / 60 / timeInterval;
                break;
            case DAYS:
                timeFactor = currentEpochSecond / 60 / 60 / 24 / timeInterval;
        }
        //把某一段时间内的请求都落到同一个Redis key上，方便统计当前时间内请求的次数
        String redisiKey = key + ":" + timeFactor;

        //Lua脚本
        //获取当前key的value，如果存在则自增1，不存在则设置value为1，并设置过期时间
        String luaScript =
                "if redis.call('exists', KEYS[1]) == 1 then " +
                        "  return redis.call('incr', KEYS[1]); " +
                        "else " +
                        "  redis.call('set', KEYS[1], 1); " +
                        "  redis.call('expire', KEYS[1], 180); " +  // 设置 180 秒过期时间
                        "  return 1; " +
                        "end";

        //执行Lua脚本
        Object countObj = redissonClient.getScript().eval(
                RScript.Mode.READ_WRITE,  //读写模式
                luaScript,//Lua脚本
                RScript.ReturnType.INTEGER,//返回类型:整数
                Collections.singletonList(redisiKey),//脚本中的KEY[1]
                expireTime //脚本中的ARGV[1]
        );

        return (long) countObj;


    }
}
