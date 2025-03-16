package com.truthgame.config.satoken;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.truthgame.mapper.UserMapper;
import com.truthgame.model.entity.User;
import com.truthgame.model.vo.LoginUserVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.truthgame.constant.UserConstant.USER_LOGIN;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {

    @Resource
    private UserMapper userMapper;

    /**
     * 返回一个账号所拥有的权限码集合 
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 根据用户角色返回权限列表
        List<String> permissionList = new ArrayList<>();

        // 获取用户角色
        User user = userMapper.selectById(loginId.toString());
        if (user != null) {
            String userRole = user.getUserRole();

            // 所有用户都有的基本权限
            permissionList.add("user.view");

            // 根据角色分配权限
            if ("admin".equals(userRole)) {
                // 管理员权限
                permissionList.add("user.add");
                permissionList.add("user.update");
                permissionList.add("user.delete");
                permissionList.add("user.get");
                permissionList.add("work.audit");
                permissionList.add("order.manage");
                permissionList.add("withdraw.audit");
                permissionList.add("admin.*");
            }
        }
        System.out.println("permissionList = " + permissionList);
        return permissionList;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        List<String> roleList = new ArrayList<>();

        // 获取用户角色
        LoginUserVO loginUserVO = (LoginUserVO) StpUtil.getSession().get(USER_LOGIN);
        if (loginUserVO != null) {
            // 将用户角色添加到角色列表中
            roleList.add(loginUserVO.getUserRole());
        }
        System.out.println("roleList = " + roleList);
        return roleList;
    }

}
