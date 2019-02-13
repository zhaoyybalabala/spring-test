package com.demo.spring;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.DefaultResourceLoader;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/13 12:19
 */
public class BeanFactoryTest {

    public static void main(String[] args) {
        //注册中心
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //读取器
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        //设置资源加载器
        reader.setResourceLoader(new DefaultResourceLoader());
        //装载构建bean的定义
        reader.loadBeanDefinitions("spring.xml");
        //打印
        System.out.println(beanFactory.getBean("di"));
        System.out.println(beanFactory.getBean("di2"));
    }
}
