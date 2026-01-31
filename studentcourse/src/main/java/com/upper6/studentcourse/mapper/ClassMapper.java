package com.upper6.studentcourse.mapper;

import com.upper6.studentcourse.model.entity.ClassEntity;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ClassMapper {

    List<ClassEntity> selectAll();

    ClassEntity selectById(@Param("id") Long id);

    int insert(ClassEntity entity);

    int updateById(ClassEntity entity);

    int deleteById(@Param("id") Long id);
}
