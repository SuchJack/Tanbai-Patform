package com.truthgame.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.truthgame.model.dto.WxLoginDTO;
import com.truthgame.model.entity.User;
import com.truthgame.model.dto.UserUpdateDTO;
import com.truthgame.model.vo.UserVO;

public interface UserService extends IService<User> {
    // 微信登录
    User wxLogin(WxLoginDTO loginDTO);
    
    // 根据openId获取用户
    User getUserByOpenId(String openId);
    
    // 创建用户
    User createUser(String openId, String unionId, String sessionKey);
    
    // 处理微信用户信息
    User processUserInfo(String sessionKey, String encryptedData, String iv, String signature, String rawData);
    
    // 更新用户信息
    UserVO updateUserInfo(UserUpdateDTO updateDTO);
} 