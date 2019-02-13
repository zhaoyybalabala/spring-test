package com.demo.spring;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/12 15:50
 */
public class HelloSpring {
    private String name;
    private int age;
    private DI di;

    public HelloSpring() {
    }

    public HelloSpring(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public DI getDi() {
        return di;
    }

    public void setDi(DI di) {
        this.di = di;
    }

    //静态工厂以及AB测试
    public static HelloSpring buid(String type) {
        if ("A".equals(type)) {
            return new HelloSpring("zyy",18);
        } else if ("B".equals(type)) {
            return new HelloSpring("xxx",20);
        } else {
           throw new IllegalArgumentException("argument must A or B");
        }

    }
}
