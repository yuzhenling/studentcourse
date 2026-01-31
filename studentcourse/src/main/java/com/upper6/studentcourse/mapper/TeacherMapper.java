package com.upper6.studentcourse.mapper;

import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.entity.Teacher;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeacherMapper {

    Teacher selectById(@Param("id") Long id);
    Teacher selectByTeacherId(@Param("teacherId") String teacherId);

    Teacher selectByTeacherIdWithPassword(@Param("teacherId") String teacherId);

    List<Teacher> selectPage(@Param("param") PageParam pageParam,
                             @Param("teacherId") String teacherId,
                             @Param("name") String name,
                             @Param("department") String department,
                             @Param("status") String status);

    long countPage(@Param("teacherId") String teacherId,
                   @Param("name") String name,
                   @Param("department") String department,
                   @Param("status") String status);

    int insert(Teacher teacher);
    int updateById(Teacher teacher);
    int updatePassword(@Param("id") Long id, @Param("passwordHash") String passwordHash);
    int deleteById(@Param("id") Long id);
}
