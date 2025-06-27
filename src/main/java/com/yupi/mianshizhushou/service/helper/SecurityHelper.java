package com.yupi.mianshizhushou.service.helper;

import cn.dev33.satoken.stp.StpUtil;
import com.yupi.mianshizhushou.common.ErrorCode;
import com.yupi.mianshizhushou.exception.BusinessException;
import com.yupi.mianshizhushou.manager.CounterManager;
import com.yupi.mianshizhushou.model.entity.User;
import com.yupi.mianshizhushou.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: lhw
 * 检测爬虫
 */
@Service
public class SecurityHelper {

    @Resource
    private CounterManager counterManager;

    @Resource
    private UserService userService;

    public void checkSpider(long userId) {
        final int WANT_COUNT = 10;
        final int BARND_COUNT = 20;
        String key = String.format("user:access:%s", userId);
        long count = counterManager.increment(key, 60, TimeUnit.SECONDS, 180);
        if (count > WANT_COUNT) {
            //踢下线
            StpUtil.kickout(userId);
            //封号
            User updateUser = new User();
            updateUser.setId(userId);
            updateUser.setUserRole("ban");
            userService.updateById(updateUser);
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "访问太过频繁，已被封号");
        }
        if (count == BARND_COUNT) {
            throw new BusinessException(110, "访问太过频繁");
        }
    }

}
