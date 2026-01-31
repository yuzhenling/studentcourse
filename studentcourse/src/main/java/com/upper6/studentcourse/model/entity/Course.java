package com.upper6.studentcourse.model.entity;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Data;

/**
 * 课程实体
 */
@Data
public class Course {

    private Long id;
    private String courseCode;
    private String courseName;
    private BigDecimal credits;
    private Integer courseHours;
    private String courseType;
    private String description;
    private Integer maxCapacity;
    private Integer currentEnrollment;
    private String semester;
    private Integer year;
    private Long teacherId;
    private String scheduleInfo;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;

    /** 教师姓名（关联查询，非表字段） */
    private String teacherName;
}
