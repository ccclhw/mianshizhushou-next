package com.yupi.mianshizhushou.satoken;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: lhw
 * @Date: 2025-06-25 - 06 - 25 - 17:26
 * @Description: com.yupi.mianshizhushou.satoken
 * @version: 1.0
 */
@RestController
@RequestMapping("/test/user/")
public class TestSaTokenLoginController {

    @RequestMapping("login")
    public String login(String username, String password) {
        if ("zhang".equals(username) && "123456".equals(password)) {
            //Sa-Token为这个账号创建一个Token凭证，通过Cookie上下文返回给了前端
            StpUtil.login(1001);
            //该方法利用了Cookie自动注入的特性，省略了获取Cookie的步骤
            return "登录成功";
        }
        return "登录失败";
    }

    //查询登录状态
    @RequestMapping("isLogin")
    public String isLogin() {
        return "当前登录状态：" + StpUtil.isLogin();
    }

    @RequestMapping("logout")
    public String logout() {
        StpUtil.logout();
        return "登出成功";
    }

}
