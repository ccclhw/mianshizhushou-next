package com.yupi.mianshizhushou.sentinel;

/**
 * @Auther: lhw
 * @Date: 2025-06-21 - 06 - 21 - 11:19
 * @Description: com.yupi.mianshizhushou.sentinel
 * @version: 1.0
 */

/**
 * Sentinel 限流熔断常量
 */
public interface SentinelConstant {

    /**
     * 分页获取题库列表接口限流
     */
    String listQuestionBankVOByPage = "listQuestionBankVOByPage";

    /**
     * 分页获取题目列表接口限流
     */
    String listQuestionVOByPage = "listQuestionVOByPage";
}

