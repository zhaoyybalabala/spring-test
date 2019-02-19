package com.demo.spring.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * com.demo.spring.service
 *
 * @author Zyy
 * @date 2019/2/14 21:58
 */
@Service
public class AccountServiceImpl implements AccountService{
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addAccount(String name, int money) {
        String accountid = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        jdbcTemplate.update("insert into account (accountname,user,money) values (?,?,?)", accountid, name, money);
        int i = 1/0;
    }

    @Transactional
    public List<Map<String, Object>> queryAccount(String name) {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from account where user = ?", name);
        return list;
    }

    @Transactional
    public int updateAccount(String name, int money) {
        return jdbcTemplate.update("update account set money = money + ? where user = ?", money, name);
    }
}
