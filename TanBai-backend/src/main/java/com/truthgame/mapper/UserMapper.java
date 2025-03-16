package com.truthgame.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.truthgame.model.entity.User;
import org.apache.ibatis.annotations.Select;

/**
* @author i7 12700KF
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2025-03-14 20:15:59
* @Entity com.truthgame.model.entity.User
*/
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据openid查询用户
     * @param openId
     * @return
     */
    @Select("select * from user where openId = #{openId}")
    User getByOpenid(String openId);

}




