package com.tanxincheng.zenith.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanxincheng.zenith.member.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
