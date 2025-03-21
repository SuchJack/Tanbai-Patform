package com.truthgame.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import com.truthgame.model.entity.CustomerService;
import com.truthgame.service.CustomerServiceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 客服服务接口
 */
@RestController
@RequestMapping("/customer-service")
public class CustomerServiceController {
    
    @Resource
    private CustomerServiceService customerServiceService;

    @SaIgnore
    @GetMapping
    public CustomerService getCustomerServiceInfo() {
        return customerServiceService.getCustomerServiceInfo();
    }

} 