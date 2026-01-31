package com.upper6.studentcourse.config;

import com.upper6.studentcourse.model.entity.SysUser;
import com.upper6.studentcourse.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 启动时确保默认管理员存在（用户名 admin，密码 admin123）
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final SysUserMapper sysUserMapper;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner seedAdmin() {
        return args -> {
            if (sysUserMapper.selectByUsername("admin") == null) {
                SysUser admin = new SysUser();
                admin.setUsername("admin");
                admin.setPasswordHash(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                admin.setRefId(null);
                admin.setStatus("ACTIVE");
                sysUserMapper.insert(admin);
                log.info("Default admin created: username=admin, password=admin123");
            }
        };
    }
}
