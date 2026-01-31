package com.upper6.studentcourse.mapper;

import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.entity.Course;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CourseMapper {

    Course selectById(@Param("id") Long id);
    Course selectByCourseCode(@Param("courseCode") String courseCode, @Param("semester") String semester);

    List<Course> selectPage(@Param("param") PageParam pageParam,
                            @Param("courseCode") String courseCode,
                            @Param("courseName") String courseName,
                            @Param("semester") String semester,
                            @Param("teacherId") Long teacherId,
                            @Param("status") String status);

    long countPage(@Param("courseCode") String courseCode,
                   @Param("courseName") String courseName,
                   @Param("semester") String semester,
                   @Param("teacherId") Long teacherId,
                   @Param("status") String status);

    int insert(Course course);
    int updateById(Course course);
    int deleteById(@Param("id") Long id);
    int updateCurrentEnrollment(@Param("id") Long id, @Param("delta") int delta);
}
