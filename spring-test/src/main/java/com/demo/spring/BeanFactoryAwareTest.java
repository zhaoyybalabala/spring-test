package com.demo.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/12 22:24
 */
public class BeanFactoryAwareTest implements BeanFactoryAware {

    private BeanFactory beanFactory;

    public void create() {
        beanFactory.getBean(DI.class).inject();
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
