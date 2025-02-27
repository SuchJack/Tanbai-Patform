package com.truthgame.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.truthgame.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where open_id = #{openid}")
    User getByOpenid(String openid);
}