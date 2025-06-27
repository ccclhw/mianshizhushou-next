package com.yupi.mianshizhushou.blackfilter;

import com.yupi.mianshizhushou.utils.NetUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Auther: lhw
 * @Date: 2025-06-21 - 06 - 21 - 18:35
 * @Description: com.yupi.mianshizhushou.blackfilter
 * @version: 1.0
 */
@WebFilter(urlPatterns = "/*", filterName = "blackIpFilter")
public class BlackIpFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        //获取请求的IP地址
        String ipAddress = NetUtils.getIpAddress((HttpServletRequest) servletRequest);
        //判断该IP是否在黑名单中
        if (BlackIpUtils.isBlackIp(ipAddress)) {
            servletResponse.setContentType("text/json;charset=UTF-8");
            servletResponse.getWriter().write("{\"errorCode\":\"-1\",\"errorMsg\":\"黑名单IP，禁止访问\"}");
            return;
        }
        //不在直接放行
        filterChain.doFilter(servletRequest, servletResponse);
    }

}

