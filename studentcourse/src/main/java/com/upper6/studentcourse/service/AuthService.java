package com.upper6.studentcourse.service;

import com.upper6.studentcourse.config.security.AuthPrincipal;
import com.upper6.studentcourse.config.security.JwtTokenProvider;
import com.upper6.studentcourse.exception.BusinessException;
import com.upper6.studentcourse.model.dto.LoginRequest;
import com.upper6.studentcourse.model.dto.LoginResponse;
import com.upper6.studentcourse.model.entity.Student;
import com.upper6.studentcourse.model.entity.SysUser;
import com.upper6.studentcourse.model.entity.Teacher;
import com.upper6.studentcourse.mapper.StudentMapper;
import com.upper6.studentcourse.mapper.SysUserMapper;
import com.upper6.studentcourse.mapper.TeacherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务：按 type 登录（ADMIN=sys_user，STUDENT=学号+students，TEACHER=工号+teachers）、修改密码
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final SysUserMapper sysUserMapper;
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request) {
        String type = request.getType() != null ? request.getType().toUpperCase() : "ADMIN";
        String username = request.getUsername();
        String password = request.getPassword();

        if ("STUDENT".equals(type)) {
            Student s = studentMapper.selectByStudentIdWithPassword(username);
            if (s == null || s.getPasswordHash() == null || !passwordEncoder.matches(password, s.getPasswordHash())) {
                throw new BusinessException(401, "学号或密码错误");
            }
            String token = jwtTokenProvider.generateToken(s.getStudentId(), "STUDENT", s.getId(), s.getId());
            return LoginResponse.builder()
                    .token(token)
                    .username(s.getStudentId())
                    .role("STUDENT")
                    .userId(s.getId())
                    .refId(s.getId())
                    .build();
        }
        if ("TEACHER".equals(type)) {
            Teacher t = teacherMapper.selectByTeacherIdWithPassword(username);
            if (t == null || t.getPasswordHash() == null || !passwordEncoder.matches(password, t.getPasswordHash())) {
                throw new BusinessException(401, "工号或密码错误");
            }
            String token = jwtTokenProvider.generateToken(t.getTeacherId(), "TEACHER", t.getId(), t.getId());
            return LoginResponse.builder()
                    .token(token)
                    .username(t.getTeacherId())
                    .role("TEACHER")
                    .userId(t.getId())
                    .refId(t.getId())
                    .build();
        }
        // ADMIN 或未传 type
        SysUser user = sysUserMapper.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole(), user.getId(), user.getRefId());
        return LoginResponse.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole())
                .userId(user.getId())
                .refId(user.getRefId())
                .build();
    }

    /** 按当前用户角色修改密码（学生/教师改各自表，管理员改 sys_user） */
    public void updatePasswordByRole(AuthPrincipal principal, String oldPassword, String newPassword) {
        String role = principal.getRole();
        Long refId = principal.getRefId();
        Long userId = principal.getUserId();
        if ("STUDENT".equals(role) && refId != null) {
            Student s = studentMapper.selectByStudentIdWithPassword(principal.getUsername());
            if (s == null || !passwordEncoder.matches(oldPassword, s.getPasswordHash())) {
                throw new BusinessException(400, "原密码错误");
            }
            studentMapper.updatePassword(refId, passwordEncoder.encode(newPassword));
            return;
        }
        if ("TEACHER".equals(role) && refId != null) {
            Teacher t = teacherMapper.selectByTeacherIdWithPassword(principal.getUsername());
            if (t == null || !passwordEncoder.matches(oldPassword, t.getPasswordHash())) {
                throw new BusinessException(400, "原密码错误");
            }
            teacherMapper.updatePassword(refId, passwordEncoder.encode(newPassword));
            return;
        }
        // ADMIN
        SysUser user = sysUserMapper.selectByUsername(principal.getUsername());
        if (user == null || !passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new BusinessException(400, "原密码错误");
        }
        sysUserMapper.updatePassword(user.getId(), passwordEncoder.encode(newPassword));
    }
}
