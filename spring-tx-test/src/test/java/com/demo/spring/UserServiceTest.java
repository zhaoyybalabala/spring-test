package com.demo.spring;

import com.demo.spring.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/14 22:15
 */
public class UserServiceTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("spring-tx.xml");
        UserService userService = context.getBean(UserService.class);
        userService.addUser("ayang");
    }
}
