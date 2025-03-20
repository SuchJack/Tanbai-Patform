package com.truthgame.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.truthgame.config.WxMpConfiguration;
import com.truthgame.exception.BusinessException;
import com.truthgame.mapper.UserMapper;
import com.truthgame.model.dto.UserUpdateDTO;
import com.truthgame.model.dto.WxLoginDTO;
import com.truthgame.model.entity.User;
import com.truthgame.model.vo.LoginUserVO;
import com.truthgame.service.UserService;
import com.truthgame.utils.SqlUtils;
import com.truthgame.utils.WxDataDecryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.truthgame.constant.UserConstant.DEFAULT_AVATAR;
import static com.truthgame.constant.UserConstant.DEFAULT_NICKNAME;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Resource
    private UserMapper userMapper;
    @Resource
    private WxMpConfiguration wxMpConfiguration;


    @Override
    public User wxLogin(WxLoginDTO loginDTO) {
        Map<String, String> wxInfo = getWxInfo(loginDTO.getCode());
        String openId = wxInfo.get("openid");
        String sessionKey = wxInfo.get("sessionKey");
        String unionId = wxInfo.get("unionid");

        //判断openid是否为空，如果为空表示登录失败，抛出业务异常
        if (openId == null) {
            throw new RuntimeException("登录失败");
        }

        //判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openId);

        //如果是新用户，自动完成注册
        if (user == null) {
            user = User.builder()
                    .openId(openId)
                    .unionId(unionId)
                    .sessionKey(sessionKey)
                    .createTime(LocalDateTime.now())
                    .avatarUrl(DEFAULT_AVATAR)
                    .nickName(DEFAULT_NICKNAME)
                    .build();
            userMapper.insert(user);
        } else {
            // 更新已有用户的sessionKey和unionId(如果存在)
            boolean needUpdate = false;

            if (sessionKey != null && !sessionKey.equals(user.getSessionKey())) {
                user.setSessionKey(sessionKey);
                needUpdate = true;
            }

            if (unionId != null && !unionId.equals(user.getUnionId())) {
                user.setUnionId(unionId);
                needUpdate = true;
            }

            if (needUpdate) {
                user.setUpdateTime(LocalDateTime.now());
                updateById(user);
            }
        }

        //返回这个用户对象
        return user;
    }

    @Override
    public User getUserByOpenId(String openId) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenId, openId);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User createUser(String openId, String unionId, String sessionKey) {
        User user = new User();
        user.setOpenId(openId);
        user.setUnionId(unionId);
        user.setSessionKey(sessionKey);
        userMapper.insert(user);
        return user;
    }

    private Map<String, String> getWxInfo(String code) {
        //调用微信接口服务，获得当前微信用户的openid
        Map<String, Object> map = new HashMap<>();
        map.put("appid", wxMpConfiguration.getAppId());
        map.put("secret", wxMpConfiguration.getAppSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");

        try {
            String json = HttpUtil.get(WX_LOGIN, map);
            log.info("微信登录返回: {}", json);

            JSONObject jsonObject = JSONUtil.parseObj(json);

            // 检查是否有错误码
            if (jsonObject.containsKey("errcode") && jsonObject.getInt("errcode") != 0) {
                log.error("微信登录失败: errcode={}, errmsg={}",
                        jsonObject.getInt("errcode"),
                        jsonObject.getStr("errmsg"));
                throw new BusinessException("微信登录失败: " + jsonObject.getStr("errmsg"));
            }

            String openid = jsonObject.getStr("openid");
            String sessionKey = jsonObject.getStr("session_key");
            String unionid = jsonObject.getStr("unionid");

//            log.info("openid = {}", openid);
//            log.info("sessionKey = {}", sessionKey);
//            log.info("unionid = {}", unionid);

            if (openid == null) {
                log.error("获取openid失败，微信返回: {}", json);
                throw new BusinessException("获取用户信息失败");
            }

            Map<String, String> result = new HashMap<>();
            result.put("openid", openid);
            result.put("sessionKey", sessionKey);
            result.put("unionid", unionid);

            return result;
        } catch (Exception e) {
            log.error("调用微信接口异常", e);
            throw new BusinessException("微信服务暂时不可用，请稍后再试");
        }
    }

    /**
     * 处理微信用户信息
     */
    public User processUserInfo(String sessionKey, String encryptedData, String iv, String signature, String rawData) {
        // 1. 校验签名
        if (!WxDataDecryptUtil.checkSignature(sessionKey, rawData, signature)) {
            throw new BusinessException("签名校验失败");
        }

        // 2. 解密数据
        String decryptedData = WxDataDecryptUtil.decrypt(sessionKey, encryptedData, iv);

        // 3. 校验水印
        if (!WxDataDecryptUtil.checkWatermark(decryptedData, wxMpConfiguration.getAppId())) {
            throw new BusinessException("数据水印校验失败");
        }

        // 4. 解析数据（使用Jackson或其他JSON工具）
        JSONObject jsonObject = JSONUtil.parseObj(decryptedData);
        String openId = jsonObject.getStr("openId");
        String unionId = jsonObject.getStr("unionId");
        String nickName = jsonObject.getStr("nickName");
        String avatarUrl = jsonObject.getStr("avatarUrl");

        // 5. 更新或创建用户
        User user = getUserByOpenId(openId);
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
        }

        user.setUnionId(unionId);
        user.setNickName(nickName);
        user.setAvatarUrl(avatarUrl);
        user.setSessionKey(sessionKey);

        // 保存用户信息
        saveOrUpdate(user);

        return user;
    }

    @Override
    public LoginUserVO updateUserInfo(UserUpdateDTO updateDTO) {
        String nickName = updateDTO.getNickName();
        boolean b = SqlUtils.validSortField(nickName);
//        if (!b) {
//            throw new BusinessException("内容非法");
//        }
//        boolean b1 = weChatUtils.verifyContent(nickName);
//        if (!b1) {
//            throw new BusinessException("内容非法");
//        }
        // 1. 获取用户信息
        User user = getById(updateDTO.getUserId());
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 2. 更新用户信息
        boolean needUpdate = false;

        if (StringUtils.hasText(updateDTO.getNickName())) {
            user.setNickName(updateDTO.getNickName());
            needUpdate = true;
        }

        if (StringUtils.hasText(updateDTO.getAvatarUrl())) {
            user.setAvatarUrl(updateDTO.getAvatarUrl());
            needUpdate = true;
        }

        // 3. 如果有更新，保存到数据库
        if (needUpdate) {
            user.setUpdateTime(LocalDateTime.now());
            updateById(user);
        }

        // 4. 生成新token
        String tokenValue = StpUtil.getTokenValue();

        // 5. 构建返回对象
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setUserId(user.getId());
        loginUserVO.setNickName(user.getNickName());
        loginUserVO.setAvatarUrl(user.getAvatarUrl());
        loginUserVO.setTokenValue(tokenValue);

        return loginUserVO;
    }

    @Override
    public LoginUserVO getLoginUserVO() {
        // 获取当前登录用户ID
        Long userId = StpUtil.getLoginIdAsLong();

        // 从数据库获取用户信息
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 获取当前token
        String tokenValue = StpUtil.getTokenValue();

        return LoginUserVO.builder()
                .userId(user.getId())
                .openId(user.getOpenId())
                .tokenValue(tokenValue)
                .avatarUrl(user.getAvatarUrl())
                .nickName(user.getNickName())
                .build();
    }
} 