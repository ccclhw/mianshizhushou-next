package com.yupi.mianshizhushou.satoken;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.yupi.mianshizhushou.constant.UserConstant;
import com.yupi.mianshizhushou.model.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义权限验证接口实现
 * 每次调用鉴权代码都会执行此方法，返回当前登录用户权限列表和角色列表
 */
@Component
public class StpInterfaceImpl implements StpInterface {
    /**
     * 返回一个账号所拥有的权限码集合 目前没用
     */
    @Override
    public List<String> getPermissionList(Object loginId, String s) {
        return new ArrayList<>();
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     *
     * @param loginId   账号id 你在调用StpUtil.login()的时候传递的loginId
     * @param loginType 账号体系标识，比如：用户登录时传递的loginType = "user"，Staff登录时传递的loginType = "staff"
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        //从当前用户的登录信息中获取角色
        //User user = (User)StpUtil.getSessionByLoginId(loginId).get(UserConstant.USER_LOGIN_STATE);
        // 从会话中获取 JSON 字符串
        String userJson = (String) StpUtil.getSessionByLoginId(loginId).get(UserConstant.USER_LOGIN_STATE);

        // 反序列化为 User 对象（此时是当前类加载器下的 User）
        User user = JSON.parseObject(userJson, User.class);
        return Collections.singletonList(user.getUserRole());
    }
}
