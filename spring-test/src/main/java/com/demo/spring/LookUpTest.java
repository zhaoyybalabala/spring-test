package com.demo.spring;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/12 18:20
 */
public abstract class LookUpTest {

    public void create() {
        getDi().inject();
    }

    //这个抽象方法由spring采用cglib（动态字节码）进行实现
    public abstract DI getDi();
}
