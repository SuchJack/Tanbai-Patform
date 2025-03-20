package com.truthgame.config.satoken;

import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.json.JSONUtil;
import com.truthgame.constant.RedisConstants;
import com.truthgame.mapper.UserMapper;
import com.truthgame.model.entity.User;
import com.truthgame.model.vo.LoginUserVO;
import com.truthgame.utils.IpUtil;
import com.truthgame.utils.RedisUtil;
import com.truthgame.utils.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 自定义侦听器的实现 
 */
@Component
@RequiredArgsConstructor
public class MySaTokenListener implements SaTokenListener {

    private final HttpServletRequest request;

    private final UserMapper userMapper;

    private final RedisUtil redisUtil;

    @Value("${sa-token.timeout}")
    private Integer timeout;

    /** 每次登录时触发 */
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {

        String ip = IpUtil.getIp();
        User user = userMapper.selectById((Long) loginId);
        // 更新登录信息
        String userAgent = request.getHeader("User-Agent");
        user.setLastLoginTime(LocalDateTime.now());
        user.setIp(ip);
        user.setIpLocation(IpUtil.getIp2region(ip));
        user.setOs(UserAgentUtil.getOs(userAgent));
        user.setBrowser(UserAgentUtil.getBrowser(userAgent));
        userMapper.updateById(user);

        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        loginUserVO.setTokenValue(tokenValue);
//        loginUserVO.setUserPassword("");
        redisUtil.set(RedisConstants.LOGIN_TOKEN + tokenValue, JSONUtil.toJsonStr(loginUserVO),timeout, TimeUnit.SECONDS);

        System.out.println("---------- 自定义侦听器实现 doLogin");
    }

    /** 每次注销时触发 */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        redisUtil.delete(RedisConstants.LOGIN_TOKEN + tokenValue);
        System.out.println("---------- 自定义侦听器实现 doLogout");
    }

    /** 每次被踢下线时触发 */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        redisUtil.delete(RedisConstants.LOGIN_TOKEN + tokenValue);
        System.out.println("---------- 自定义侦听器实现 doKickout");
    }

    /** 每次被顶下线时触发 */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        redisUtil.delete(RedisConstants.LOGIN_TOKEN + tokenValue);
        System.out.println("---------- 自定义侦听器实现 doReplaced");
    }

    /** 每次被封禁时触发 */
    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {
        System.out.println("---------- 自定义侦听器实现 doDisable");
    }

    /** 每次被解封时触发 */
    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {
        System.out.println("---------- 自定义侦听器实现 doUntieDisable");
    }

    /** 每次二级认证时触发 */
    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {
        System.out.println("---------- 自定义侦听器实现 doOpenSafe");
    }

    /** 每次退出二级认证时触发 */
    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {
        System.out.println("---------- 自定义侦听器实现 doCloseSafe");
    }

    /** 每次创建Session时触发 */
    @Override
    public void doCreateSession(String id) {
        System.out.println("---------- 自定义侦听器实现 doCreateSession");
    }

    /** 每次注销Session时触发 */
    @Override
    public void doLogoutSession(String id) {
        System.out.println("---------- 自定义侦听器实现 doLogoutSession");
    }
    
    /** 每次Token续期时触发 */
    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {
        System.out.println("---------- 自定义侦听器实现 doRenewTimeout");
    }
}
