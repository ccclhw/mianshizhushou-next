package com.yupi.mianshizhushou.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.mianshizhushou.model.dto.questionBankQuestion.QuestionBankQuestionQueryRequest;
import com.yupi.mianshizhushou.model.entity.QuestionBankQuestion;
import com.yupi.mianshizhushou.model.entity.User;
import com.yupi.mianshizhushou.model.vo.QuestionBankQuestionVO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 题库题目服务
 *

 */
public interface QuestionBankQuestionService extends IService<QuestionBankQuestion> {

    /**
     * 校验数据
     *
     * @param questionBankQuestion
     * @param add 对创建的数据进行校验
     */
    void validQuestionBankQuestion(QuestionBankQuestion questionBankQuestion, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionBankQuestionQueryRequest
     * @return
     */
    QueryWrapper<QuestionBankQuestion> getQueryWrapper(QuestionBankQuestionQueryRequest questionBankQuestionQueryRequest);

    /**
     * 获取题库题目封装
     *
     * @param questionBankQuestion
     * @param request
     * @return
     */
    QuestionBankQuestionVO getQuestionBankQuestionVO(QuestionBankQuestion questionBankQuestion, HttpServletRequest request);

    /**
     * 分页获取题库题目封装
     *
     * @param questionBankQuestionPage
     * @param request
     * @return
     */
    Page<QuestionBankQuestionVO> getQuestionBankQuestionVOPage(Page<QuestionBankQuestion> questionBankQuestionPage, HttpServletRequest request);

    /**
     * 批量向题库中添加题目
     *
     * @param questionIdList 题目列表
     * @param questionBankId 题库id
     * @param loginUser      登录的用户
     */
    void batchAddQuestionsToBank(List<Long> questionIdList, Long questionBankId, User loginUser);

    // 分批处理 避免长事务问题
    // 分批事务若处理失败 只需回滚一批就可 降低了长事务带来的资源消耗，同时提升了稳定性
    @Transactional(rollbackFor = Exception.class)
    void batchAddQuestionsToBankInner(List<QuestionBankQuestion> questionBankQuestionList);

    /**
     * 批量从题库中移除题目
     *
     * @param questionIdList
     * @param questionBankId
     */
    void batchRemoveQuestionsToBank(List<Long> questionIdList, Long questionBankId);
}
