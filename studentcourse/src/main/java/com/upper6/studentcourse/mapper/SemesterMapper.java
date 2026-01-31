package com.upper6.studentcourse.mapper;

import com.upper6.studentcourse.model.entity.Semester;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SemesterMapper {

    List<Semester> selectAll();
    Semester selectBySemester(@Param("semester") String semester);
    Semester selectCurrent();
}
