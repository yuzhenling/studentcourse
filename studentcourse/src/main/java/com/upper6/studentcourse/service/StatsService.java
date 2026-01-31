package com.upper6.studentcourse.service;

import com.upper6.studentcourse.mapper.StatsMapper;
import com.upper6.studentcourse.model.dto.StatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 统计服务：管理员首页、教师首页
 */
@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsMapper statsMapper;

    public StatsDTO.AdminDashboard getAdminDashboard() {
        List<StatsDTO.DeptCount> coursesByDepartment = statsMapper.countCoursesByDepartment();
        List<StatsDTO.YearCount> studentsByYear = statsMapper.countStudentsByYear();
        List<StatsDTO.TeacherCourseCount> coursesByTeacher = statsMapper.countCoursesByTeacher();
        StatsDTO.PassRate passRateOverall = statsMapper.passRateOverall();
        if (passRateOverall == null) {
            passRateOverall = new StatsDTO.PassRate(0L, 0L, BigDecimal.ZERO);
        }
        List<StatsDTO.PassRateItem> passRateByDepartment = statsMapper.passRateByDepartment();
        List<StatsDTO.PassRateItem> passRateByTeacher = statsMapper.passRateByTeacher();
        return StatsDTO.AdminDashboard.builder()
                .coursesByDepartment(coursesByDepartment)
                .studentsByYear(studentsByYear)
                .coursesByTeacher(coursesByTeacher)
                .passRateOverall(passRateOverall)
                .passRateByDepartment(passRateByDepartment)
                .passRateByTeacher(passRateByTeacher)
                .build();
    }

    public StatsDTO.TeacherDashboard getTeacherDashboard(Long teacherId) {
        StatsDTO.PassRate passRateOverall = statsMapper.passRateByTeacherId(teacherId);
        if (passRateOverall == null) {
            passRateOverall = new StatsDTO.PassRate(0L, 0L, BigDecimal.ZERO);
        }
        List<StatsDTO.PassRateByCourse> passRateByCourse = statsMapper.passRateByTeacherIdGroupByCourse(teacherId);
        return StatsDTO.TeacherDashboard.builder()
                .passRateOverall(passRateOverall)
                .passRateByCourse(passRateByCourse)
                .build();
    }
}
