package com.yupi.mianshizhushou.job.cycle;

import com.yupi.mianshizhushou.esdao.QuestionEsDao;
import com.yupi.mianshizhushou.mapper.PostMapper;
import com.yupi.mianshizhushou.mapper.QuestionMapper;
import com.yupi.mianshizhushou.model.dto.post.PostEsDTO;
import com.yupi.mianshizhushou.model.dto.question.QuestionEsDTO;
import com.yupi.mianshizhushou.model.entity.Post;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.yupi.mianshizhushou.model.entity.Question;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.core.collection.CollUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 增量同步题目到 es
 *
 * 
 */
// todo 取消注释开启任务
@Component
@Slf4j
public class IncSyncQuestionToEs {

    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionEsDao questionEsDao;

    /**
     * 每分钟执行一次
     */
    @Scheduled(fixedRate = 60 * 1000)
    public void run() {
        // 查询近 5 分钟内的数据
        Date fiveMinutesAgoDate = new Date(new Date().getTime() - 5 * 60 * 1000L);
        //假设当前是11:10分  updateTime > 2025-06-05 11:05:00
        List<Question> questionList = questionMapper.listQuestionWithDelete(fiveMinutesAgoDate);
        if (CollUtil.isEmpty(questionList)) {
            log.info("no inc question");
            return;
        }
        List<QuestionEsDTO> questionEsDTOList = questionList.stream()
                .map(QuestionEsDTO::objToDto)
                .collect(Collectors.toList());
        final int pageSize = 500;//页大小
        int total = questionEsDTOList.size();
        log.info("IncSyncQuestionToEs start, total {}", total);
        for (int i = 0; i < total; i += pageSize) {
            int end = Math.min(i + pageSize, total);
            log.info("sync from {} to {}", i, end);
            questionEsDao.saveAll(questionEsDTOList.subList(i, end));
        }
        log.info("IncSyncQuestionToEs end, total {}", total);
    }
}
