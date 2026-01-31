package com.upper6.studentcourse.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 统计接口 DTO
 */
public class StatsDTO {

    /** 院系/专业 + 数量 */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeptCount {
        private String department;
        private Long count;
    }

    /** 学年 + 学生数 */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class YearCount {
        private Integer year;
        private Long count;
    }

    /** 教师 + 课程数 */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeacherCourseCount {
        private Long teacherId;
        private String teacherName;
        private Long count;
    }

    /** 通过率：总人数、通过人数、通过率 */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassRate {
        private Long total;
        private Long passed;
        private BigDecimal rate;
    }

    /** 按维度通过率（院系/教师/课程） */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassRateItem {
        private String name;
        private Long total;
        private Long passed;
        private BigDecimal rate;
    }

    /** 按课程通过率（教师端） */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassRateByCourse {
        private Long courseId;
        private String courseName;
        private Long total;
        private Long passed;
        private BigDecimal rate;
    }

    /** 管理员首页统计 */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdminDashboard {
        private List<DeptCount> coursesByDepartment;
        private List<YearCount> studentsByYear;
        private List<TeacherCourseCount> coursesByTeacher;
        private PassRate passRateOverall;
        private List<PassRateItem> passRateByDepartment;
        private List<PassRateItem> passRateByTeacher;
    }

    /** 教师首页统计 */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeacherDashboard {
        private PassRate passRateOverall;
        private List<PassRateByCourse> passRateByCourse;
    }
}
