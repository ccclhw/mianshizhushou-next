package com.yupi.mianshizhushou.config;


import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissionConfig {

    private String host;//ip地址

    private Integer port;//端口号

    private Integer database;//使用redis中的几号库

    private String password;//redis密码


    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        //使用单节点模式
        config.useSingleServer()
                .setAddress("redis://"+host+":"+port)
                .setDatabase(database)
                .setPassword(password);
        return Redisson.create(config);
    }
}
