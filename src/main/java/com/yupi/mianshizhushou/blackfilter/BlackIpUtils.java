package com.yupi.mianshizhushou.blackfilter;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.util.List;
import java.util.Map;

/**
 * @Auther: lhw
 * @Date: 2025-06-21 - 06 - 21 - 17:27
 * @Description: com.yupi.mianshizhushou.blackfilter
 * @version: 1.0
 */
@Slf4j
public class BlackIpUtils {
    //使用Hutool工具库中的布隆过滤器
    private static BitMapBloomFilter bloomFilter = new BitMapBloomFilter(100);

    //判断IP是否在黑名单中
    public static boolean isBlackIp(String ip) {
        if (bloomFilter == null) {
            log.error("bloomFilter is null. Please initialize it before calling isBlackIp.");
            return false;  // 或者其他处理逻辑
        }
        return bloomFilter.contains(ip);
    }

    //重建bloomFilter
    //Nacos配置文件的监听的粒度比较粗，只能知晓配置有变更，无法知晓是新增、修改还是删除，因此进行重建
    public static void rebuildBlackIp(String configInfo) {
        if (StrUtil.isEmpty(configInfo)) {
            configInfo = "{}";
        }
        Yaml yaml = new Yaml();
        //将配置文件转为Map集合
        Map map = yaml.loadAs(configInfo, Map.class);
        //在map集合中获取黑名单ip
        List<String> blackIpList = (List<String>) map.get("blackIpList");
        synchronized (BlackIpUtils.class) {
            if (CollUtil.isNotEmpty(blackIpList)) {
                // 注意构造参数的设置
                //BitMapBloomFilter bitMapBloomFilter = new BitMapBloomFilter(958506);
                // 动态计算 Bloom Filter 大小
                int filterSize = Math.max(100, blackIpList.size() * 2);  // 根据黑名单数量计算适当的大小
                BitMapBloomFilter bitMapBloomFilter = new BitMapBloomFilter(filterSize);
                for (String blackIp : blackIpList) {
                    bitMapBloomFilter.add(blackIp);
                }
                bloomFilter = bitMapBloomFilter;
            } else {
                bloomFilter = new BitMapBloomFilter(100);
            }
        }
    }
}
