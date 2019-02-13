package com.demo.spring;

import org.springframework.beans.factory.FactoryBean;

import java.sql.Driver;
import java.sql.DriverManager;

/**
 * com.demo.spring
 *
 * @author Zyy
 * @date 2019/2/12 16:13
 */
public class DriverFactoryBean implements FactoryBean {
    private String jdbcUrl;

    public Object getObject() throws Exception {
        return DriverManager.getDriver(jdbcUrl);
    }

    public Class<?> getObjectType() {
        return Driver.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }
}
