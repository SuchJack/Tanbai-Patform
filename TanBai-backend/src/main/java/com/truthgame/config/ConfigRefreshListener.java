package com.truthgame.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 监听配置变更事件，并打印所有变更的属性。
 */
@Component
@Slf4j
public class ConfigRefreshListener {

    private final Environment environment;

    public ConfigRefreshListener(Environment environment) {
        this.environment = environment;
    }

    @EventListener
    public void onEnvironmentChangeEvent(EnvironmentChangeEvent event) {
        Set<String> keys = event.getKeys();
        log.info("配置已更新，变更的属性数量: {}", keys.size());
        
        // 打印所有变更的配置项
        for (String key : keys) {
            log.info("配置变更 - 属性: {}, 新值: {}", key, environment.getProperty(key));
        }
    }
    
    @EventListener
    public void onRefreshScopeRefreshed(RefreshScopeRefreshedEvent event) {
        log.info("RefreshScope已刷新，Bean: {}", event.getName());
    }
}