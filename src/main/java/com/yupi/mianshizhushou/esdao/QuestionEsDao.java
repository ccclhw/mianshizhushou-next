package com.yupi.mianshizhushou.esdao;

import com.yupi.mianshizhushou.model.dto.question.QuestionEsDTO;

import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 帖子 ES 操作
 *
 * 
 */
public interface QuestionEsDao extends ElasticsearchRepository<QuestionEsDTO, Long> {

    /**
     * 根据id查询数据
     *
     * @param userId
     * @return
     */
    List<QuestionEsDTO> findByUserId(Long userId);
}