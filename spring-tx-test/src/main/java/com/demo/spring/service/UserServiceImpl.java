package com.demo.spring.service;

import org.springframework.aop.framework.AopContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * com.demo.spring.service
 *
 * @author Zyy
 * @date 2019/2/14 21:53
 */
@Service
public class UserServiceImpl implements UserService{
    @Resource
    private AccountService accountService;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(String name) {
        jdbcTemplate.update("insert into user (name) values(?)", name);
        //this.addAccount(name, 100);
        ((UserService) AopContext.currentProxy()).addAccount(name, 100);
        int i = 1/0;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addAccount(String name, int money) {
        System.out.println("xxx");
        String accountid = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        jdbcTemplate.update("insert into account (accountname,user,money) values (?,?,?)", accountid, name, money);
    }

}
