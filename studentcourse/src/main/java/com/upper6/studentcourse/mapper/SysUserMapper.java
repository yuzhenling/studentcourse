package com.upper6.studentcourse.mapper;

import com.upper6.studentcourse.model.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统用户 Mapper
 */
@Mapper
public interface SysUserMapper {

    SysUser selectByUsername(@Param("username") String username);

    SysUser selectById(@Param("id") Long id);

    int updatePassword(@Param("id") Long id, @Param("passwordHash") String passwordHash);

    int insert(SysUser user);
}
