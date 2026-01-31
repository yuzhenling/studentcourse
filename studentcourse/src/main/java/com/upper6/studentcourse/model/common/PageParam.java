package com.upper6.studentcourse.model.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 分页查询参数
 */
@Data
public class PageParam {

    @Min(1)
    private Integer page = 1;

    @Min(1)
    @Max(100)
    private Integer size = 20;

    private String sortBy = "id";
    private String direction = "DESC";

    public int getOffset() {
        return (page - 1) * size;
    }
}
