package com.upper6.studentcourse.mapper;

import com.upper6.studentcourse.model.dto.StatsDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StatsMapper {

    /** 各院系开设课程数 */
    List<StatsDTO.DeptCount> countCoursesByDepartment();

    /** 按入学学年统计学生数 */
    List<StatsDTO.YearCount> countStudentsByYear();

    /** 按教师统计开设课程数 */
    List<StatsDTO.TeacherCourseCount> countCoursesByTeacher();

    /** 整体成绩通过率（已发布成绩） */
    StatsDTO.PassRate passRateOverall();

    /** 按院系统计通过率 */
    List<StatsDTO.PassRateItem> passRateByDepartment();

    /** 按教师统计通过率 */
    List<StatsDTO.PassRateItem> passRateByTeacher();

    /** 某教师整体通过率 */
    StatsDTO.PassRate passRateByTeacherId(@Param("teacherId") Long teacherId);

    /** 某教师按课程通过率 */
    List<StatsDTO.PassRateByCourse> passRateByTeacherIdGroupByCourse(@Param("teacherId") Long teacherId);
}
