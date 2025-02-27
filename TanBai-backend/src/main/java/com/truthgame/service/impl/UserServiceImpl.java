package com.truthgame.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.truthgame.Properties.JwtProperties;
import com.truthgame.config.WxConfig;
import com.truthgame.constant.JwtClaimsConstant;
import com.truthgame.model.dto.WxLoginDTO;
import com.truthgame.model.entity.User;
import com.truthgame.exception.BusinessException;
import com.truthgame.mapper.UserMapper;
import com.truthgame.service.UserService;
import com.truthgame.utils.SqlUtils;
import com.truthgame.utils.WeChatUtils;
import com.truthgame.utils.WxDataDecryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;
import com.truthgame.model.dto.UserUpdateDTO;
import com.truthgame.model.vo.UserVO;
import com.truthgame.utils.JwtUtil;

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
    private WxConfig wxConfig;

    @Resource
    private JwtProperties jwtProperties;

    @Resource
    private WeChatUtils weChatUtils;

    @Override
    public User wxLogin(WxLoginDTO loginDTO) {
        String openid = getOpenid(loginDTO.getCode());

        //判断openid是否为空，如果为空表示登录失败，抛出业务异常
        if (openid == null) {
            throw new RuntimeException("登录失败");
        }

        //判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openid);

        //如果是新用户，自动完成注册
        if(user == null){
            user = User.builder()
                    .openId(openid)
                    .createTime(LocalDateTime.now())
                    .avatarUrl(DEFAULT_AVATAR)
                    .nickName(DEFAULT_NICKNAME)
                    .build();
            userMapper.insert(user);//后绪步骤实现
        }

        //返回这个用户对象
        return user;

//        // 调用微信接口获取openId和sessionKey
//        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
//                wxConfig.getAppId(), wxConfig.getAppSecret(), loginDTO.getCode());
//        String result = HttpUtil.get(url);
//        JSONObject json = JSONUtil.parseObj(result);
//
//        if (json.containsKey("errcode") && json.getInt("errcode") != 0) {
//            throw new BusinessException("微信登录失败：" + json.getStr("errmsg"));
//        }
//
//        String openId = json.getStr("openid");
//        String unionId = json.getStr("unionid");
//        String sessionKey = json.getStr("session_key");
//
//        // 查找或创建用户
//        User user = getUserByOpenId(openId);
//        boolean isNewUser = false;
//
//        if (user == null) {
//            user = createUser(openId, unionId, sessionKey);
//            isNewUser = true;
//        }
//
//        // 生成token
//        String token = JwtUtil.generateToken(user.getId());
//
//        // 构建响应
//        LoginResponseDTO response = new LoginResponseDTO();
//        response.setUserId(user.getId());
//        response.setToken(token);
//        response.setIsNewUser(isNewUser);
//
//        return response;
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

    /**
     * 调用微信接口服务，获取微信用户的openid
     * @param code
     * @return
     */
    private String getOpenid(String code) {
        //调用微信接口服务，获得当前微信用户的openid
        Map<String, Object> map = new HashMap<>();
        map.put("appid", wxConfig.getAppId());
        map.put("secret", wxConfig.getAppSecret());
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String json = HttpUtil.get(WX_LOGIN, map);

        JSONObject jsonObject = JSONUtil.parseObj(json);
        String openid = jsonObject.getStr("openid");
        String sessionKey = jsonObject.getStr("session_key");
        log.info("openid = " + openid);
        log.info("sessionKey = " + sessionKey);
        return openid;
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
        if (!WxDataDecryptUtil.checkWatermark(decryptedData, wxConfig.getAppId())) {
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
    public UserVO updateUserInfo(UserUpdateDTO updateDTO) {
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
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(),claims);
        
        // 5. 构建返回对象
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getId());
        userVO.setNickName(user.getNickName());
        userVO.setAvatarUrl(user.getAvatarUrl());
        userVO.setToken(token);
        
        return userVO;
    }
} 