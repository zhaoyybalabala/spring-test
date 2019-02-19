package com.demo.spring;

import com.demo.spring.service.AccountService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/14 22:15
 */
public class AccountServiceTest {

    @Test
    public void add() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("spring-tx.xml");
        AccountService accountService = context.getBean(AccountService.class);
        accountService.addAccount("ayang",100);
    }

    @Test
    public void update() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("spring-tx.xml");
        AccountService accountService = context.getBean(AccountService.class);
        accountService.updateAccount("ayang",110);
    }

    @Test
    public void select() {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("spring-tx.xml");
        AccountService accountService = context.getBean(AccountService.class);
        List<Map<String, Object>> list = accountService.queryAccount("ayang");
        System.out.println(Arrays.toString(list.toArray()));
    }
}
