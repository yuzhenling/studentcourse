package com.upper6.studentcourse.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.time.Instant;
import lombok.Data;

/**
 * 学生实体
 */
@Data
public class Student {

    private Long id;
    private String studentId;
    private String name;
    private String gender;
    private LocalDate birthDate;
    private String email;
    private String phone;
    private String major;
    private Integer enrollmentYear;
    private Long classId;
    /** 班级名称（关联查询，非表字段） */
    private String className;
    @JsonIgnore
    private String passwordHash;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
