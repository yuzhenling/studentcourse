package com.upper6.studentcourse.mapper;

import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.entity.Grade;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GradeMapper {

    Grade selectById(@Param("id") Long id);
    Grade selectBySelectionId(@Param("selectionId") Long selectionId);

    List<Grade> selectByStudent(@Param("studentId") Long studentId, @Param("semester") String semester);
    List<Grade> selectPage(@Param("param") PageParam pageParam,
                          @Param("studentId") Long studentId,
                          @Param("courseId") Long courseId,
                          @Param("semester") String semester,
                          @Param("published") Boolean published);
    long countPage(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("semester") String semester, @Param("published") Boolean published);

    int insert(Grade grade);
    int updateById(Grade grade);
    int updatePublished(@Param("id") Long id, @Param("published") boolean published);
}
