package com.upper6.studentcourse.mapper;

import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.entity.CourseSelection;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CourseSelectionMapper {

    CourseSelection selectById(@Param("id") Long id);
    CourseSelection selectByStudentAndCourseAndSemester(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("semester") String semester);

    List<CourseSelection> selectByStudent(@Param("studentId") Long studentId, @Param("semester") String semester);
    List<CourseSelection> selectPage(@Param("param") PageParam pageParam,
                                    @Param("studentId") Long studentId,
                                    @Param("courseId") Long courseId,
                                    @Param("semester") String semester,
                                    @Param("status") String status);
    long countPage(@Param("studentId") Long studentId, @Param("courseId") Long courseId, @Param("semester") String semester, @Param("status") String status);

    int insert(CourseSelection cs);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
    int deleteById(@Param("id") Long id);
}
