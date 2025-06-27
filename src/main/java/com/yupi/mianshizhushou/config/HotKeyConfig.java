package com.yupi.mianshizhushou.config;

import com.jd.platform.hotkey.client.ClientStarter;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * HotKey配置
 */
@Configuration
@ConfigurationProperties(prefix = "hotkey")//从配置文件中加载属性
@Data
public class HotKeyConfig {

    /*
     * etcd服务器地址
     * */
    private String etcdServer = "http://127.0.1:2379";

    /*
     * app名称
     * */
    private String appName = "app";

    /*
     * 本地缓存最大数量
     * */
    private int caffeineSize = 10000;

    /*
     * 批量推送key的间隔时间
     * */
    private long pushPeriod = 1000L;

    /*
     * 初始化HotKey
     * */
    @Bean
    public ClientStarter initHotKey() {
        ClientStarter starter = new ClientStarter.Builder()
                .setAppName(appName)
                .setCaffeineSize(caffeineSize)
                .setEtcdServer(etcdServer)
                .setPushPeriod(pushPeriod)
                .build();
        starter.startPipeline();

        return starter;
    }


}
