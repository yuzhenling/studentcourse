package com.upper6.studentcourse.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 教师创建/更新 DTO
 */
@Data
public class TeacherDTO {

    @NotBlank(message = "工号不能为空")
    private String teacherId;

    @NotBlank(message = "姓名不能为空")
    private String name;

    private String title;
    private String department;
    private String email;
    private String phone;
    private String office;
    private String researchField;
    private String status;
}
