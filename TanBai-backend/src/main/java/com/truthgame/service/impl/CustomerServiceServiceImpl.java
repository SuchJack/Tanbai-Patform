package com.truthgame.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.truthgame.mapper.CustomerServiceMapper;
import com.truthgame.model.entity.CustomerService;
import com.truthgame.service.CustomerServiceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CustomerServiceServiceImpl implements CustomerServiceService {
    
    @Resource
    private CustomerServiceMapper customerServiceMapper;
    
    @Override
    public CustomerService getCustomerServiceInfo() {
        return customerServiceMapper.selectOne(new QueryWrapper<>());
    }
    
    @Override
    public boolean updateCustomerServiceInfo(CustomerService customerService) {
        // 如果没有记录，则插入
        if (getCustomerServiceInfo() == null) {
            return customerServiceMapper.insert(customerService) > 0;
        }
        // 如果有记录，则更新第一条记录
        QueryWrapper<CustomerService> wrapper = new QueryWrapper<>();
        return customerServiceMapper.update(customerService, wrapper) > 0;
    }
} 