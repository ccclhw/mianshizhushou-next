package com.yupi.mianshizhushou.job.once;

import com.yupi.mianshizhushou.esdao.QuestionEsDao;
import com.yupi.mianshizhushou.model.dto.post.PostEsDTO;
import com.yupi.mianshizhushou.model.dto.question.QuestionEsDTO;
import com.yupi.mianshizhushou.model.entity.Post;
import com.yupi.mianshizhushou.model.entity.Question;
import com.yupi.mianshizhushou.service.PostService;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.yupi.mianshizhushou.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.collection.CollUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 全量同步帖子到 es
 *
 * 
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class FullSyncQuestionToEs implements CommandLineRunner {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionEsDao questionEsDao;

    @Override
    public void run(String... args) {
        //查询出所有的题目
        List<Question> questionList = questionService.list();
        if (CollUtil.isEmpty(questionList)) {
            return;
        }
        //将查询的题目转换为DTO传输对象
        List<QuestionEsDTO> questionEsDTOList = questionList.stream().map(QuestionEsDTO::objToDto).collect(Collectors.toList());
        //分页批量插入数据
        final int pageSize = 500;
        int total = questionEsDTOList.size();
        log.info("FullSyncQuestionToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            //同步的数据不能超过总数据
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            questionEsDao.saveAll(questionEsDTOList.subList(i, end));
        }
        log.info("FullSyncQuestionToEs end, total {}", total);
    }
}
