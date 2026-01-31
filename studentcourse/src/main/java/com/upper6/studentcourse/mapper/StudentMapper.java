package com.upper6.studentcourse.mapper;

import com.upper6.studentcourse.model.common.PageParam;
import com.upper6.studentcourse.model.entity.Student;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 学生 Mapper
 */
@Mapper
public interface StudentMapper {

    Student selectById(@Param("id") Long id);

    Student selectByStudentId(@Param("studentId") String studentId);

    Student selectByStudentIdWithPassword(@Param("studentId") String studentId);

    List<Student> selectPage(@Param("param") PageParam pageParam,
                            @Param("studentId") String studentId,
                            @Param("name") String name,
                            @Param("major") String major,
                            @Param("status") String status);

    long countPage(@Param("studentId") String studentId,
                   @Param("name") String name,
                   @Param("major") String major,
                   @Param("status") String status);

    int insert(Student student);

    int updateById(Student student);

    int updatePassword(@Param("id") Long id, @Param("passwordHash") String passwordHash);

    int deleteById(@Param("id") Long id);
}
