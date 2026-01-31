package com.upper6.studentcourse.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 课程创建/更新 DTO
 */
@Data
public class CourseDTO {

    @NotBlank(message = "课程代码不能为空")
    private String courseCode;

    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    @NotNull(message = "学分不能为空")
    private BigDecimal credits;

    private Integer courseHours;
    private String courseType;
    private String description;
    private Integer maxCapacity;
    private String semester;
    private Integer year;
    private Long teacherId;
    private String scheduleInfo;
    private String status;
}
