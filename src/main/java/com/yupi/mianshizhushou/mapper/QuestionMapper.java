package com.yupi.mianshizhushou.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.mianshizhushou.model.entity.Question;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @author lhw
 * @description 针对表【question(题目)】的数据库操作Mapper
 * @createDate 2025-05-29 16:00:34
 * @Entity generator.domain.Question
 */
public interface QuestionMapper extends BaseMapper<Question> {

    //因为MyBatis提供的mapper方法 查询时会过滤掉isDelete=1(逻辑删除)的数据
    //为了让ES和MySQL完全同步 在QuestionMapper里面写一个能查询出 isDelete=1的方法

    @Select("select * from question where updateTime>#{minUpdateTime}")
    List<Question> listQuestionWithDelete(Date minUpdateTime);

}




