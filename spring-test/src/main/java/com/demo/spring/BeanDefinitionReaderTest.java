package com.demo.spring;

import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.SimpleBeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.util.Arrays;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/13 11:34
 */
public class BeanDefinitionReaderTest {

    public static void main(String[] args) {
        //创建一个简单注册器
        BeanDefinitionRegistry registry = new SimpleBeanDefinitionRegistry();
        //创建bean定义读取器
        BeanDefinitionReader reader = new XmlBeanDefinitionReader(registry);
        //创建资源读取器
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        //获取资源
        Resource resource = resourceLoader.getResource("spring.xml");
        //装载类定义
        reader.loadBeanDefinitions(resource);
        //打印bean定义名称
        System.out.println(Arrays.toString(registry.getBeanDefinitionNames()));

        System.out.println(registry.getBeanDefinition("di"));
        System.out.println(registry.getAliases("di2"));
    }
}
