package com.demo.spring;

import com.demo.spring.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/18 23:05
 */
public class TransactionalTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-tx.xml");
        final UserService userService = context.getBean(UserService.class);

        UserService proxyUserService = (UserService) Proxy.newProxyInstance(
                TransactionalTest.class.getClassLoader(), new Class[]{UserService.class}, new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        try {
                            System.out.println("开启事物：" + method.getName());
                            return method.invoke(userService,args);
                        } finally {
                            System.out.println("关闭事物:" + method.getName());
                        }
                    }
                });

        proxyUserService.addUser("ayang");
    }
}
