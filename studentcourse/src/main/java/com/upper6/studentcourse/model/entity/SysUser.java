package com.upper6.studentcourse.model.entity;

import java.time.Instant;
import lombok.Data;

/**
 * 系统用户实体（登录账号）
 */
@Data
public class SysUser {

    private Long id;
    private String username;
    private String passwordHash;
    private String role;
    private Long refId;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
