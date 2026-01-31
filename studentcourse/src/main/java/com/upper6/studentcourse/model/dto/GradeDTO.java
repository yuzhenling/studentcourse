package com.upper6.studentcourse.model.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class GradeDTO {

    @NotNull(message = "选课记录 ID 不能为空")
    private Long selectionId;

    private BigDecimal regularScore;
    private BigDecimal midtermScore;
    private BigDecimal finalScore;
    private BigDecimal totalScore;
    private BigDecimal gradePoint;
    private String letterGrade;
    private Boolean isPassed;
    private String teacherComment;
}
