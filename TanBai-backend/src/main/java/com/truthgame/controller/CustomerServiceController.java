package com.truthgame.controller;

import com.truthgame.model.entity.CustomerService;
import com.truthgame.service.CustomerServiceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/customer-service")
public class CustomerServiceController {
    
    @Resource
    private CustomerServiceService customerServiceService;
    
    @GetMapping
    public CustomerService getCustomerServiceInfo() {
        return customerServiceService.getCustomerServiceInfo();
    }
    
//    @PutMapping
//    public boolean updateCustomerServiceInfo(@RequestBody CustomerService customerService) {
//        return customerServiceService.updateCustomerServiceInfo(customerService);
//    }
} 