package com.truthgame.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.truthgame.TruthGameApplication;
import com.truthgame.mapper.QuestionViewPermissionMapper;
import com.truthgame.model.entity.QuestionViewPermission;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@SpringBootTest(classes = TruthGameApplication.class)
class QuestionServiceImplTest {

    @Resource
    private QuestionViewPermissionMapper questionViewPermissionMapper;

    @Test
    void testGetPermissions() {
        // 1. 测试查询权限
        LambdaQueryWrapper<QuestionViewPermission> permissionWrapper = new LambdaQueryWrapper<>();
        permissionWrapper.eq(QuestionViewPermission::getQuestionId, 23L)
                .eq(QuestionViewPermission::getUserId, 8L);
        QuestionViewPermission permission = questionViewPermissionMapper.selectOne(permissionWrapper);
        System.out.println("查询结果: " + permission);

        // 2. 测试插入权限
        if (permission == null) {
            QuestionViewPermission newPermission = new QuestionViewPermission();
            newPermission.setQuestionId(23L);
            newPermission.setUserId(8L);
            newPermission.setPaidTime(LocalDateTime.now());
            int result = questionViewPermissionMapper.insert(newPermission);
            System.out.println("插入结果: " + result);
        }

        // 3. 再次查询确认
        permission = questionViewPermissionMapper.selectOne(permissionWrapper);
        System.out.println("最终结果: " + permission);
    }
} 