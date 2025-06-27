package com.yupi.mianshizhushou.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限校验
 *
 * 
 */
@Target(ElementType.METHOD)//表示这个注解只能用于方法上
@Retention(RetentionPolicy.RUNTIME)//指定注解的保留策略为运行时
public @interface AuthCheck {

    /**
     * 必须有某个角色
     *
     * @return
     */
    String mustRole() default "";

}

