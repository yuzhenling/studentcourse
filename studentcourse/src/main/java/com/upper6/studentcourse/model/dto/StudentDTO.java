package com.upper6.studentcourse.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

/**
 * 学生创建/更新 DTO
 */
@Data
public class StudentDTO {

    @NotBlank(message = "学号不能为空")
    private String studentId;

    @NotBlank(message = "姓名不能为空")
    private String name;

    private String gender;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String major;

    @NotNull(message = "入学年份不能为空")
    private Integer enrollmentYear;

    private Long classId;
    private String status;
}
