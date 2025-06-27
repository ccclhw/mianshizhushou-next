package com.yupi.mianshizhushou.model.dto.questionBankQuestion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 批量向题库中添加题目请求
 */
@Data
public class QuestionBankQuestionBatchAddRequest implements Serializable {

    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id 集合
     */
    private List<Long> questionIdList;

    private static final long serialVersionUID = 1L;
}