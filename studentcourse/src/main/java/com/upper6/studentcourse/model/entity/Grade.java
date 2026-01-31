package com.upper6.studentcourse.model.entity;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Data;

/**
 * 成绩实体
 */
@Data
public class Grade {

    private Long id;
    private Long selectionId;
    private BigDecimal regularScore;
    private BigDecimal midtermScore;
    private BigDecimal finalScore;
    private BigDecimal totalScore;
    private BigDecimal gradePoint;
    private String letterGrade;
    private Boolean isPassed;
    private String teacherComment;
    private Boolean published;
    private Instant publishedDate;
    private Instant createdAt;
    private Instant updatedAt;

    private String studentName;
    private String courseName;
    private String courseCode;
}
