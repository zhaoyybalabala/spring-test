package com.demo.spring.service;

import java.util.List;
import java.util.Map;

/**
 * com.demo.spring.service
 *
 * @author Zyy
 * @date 2019/2/14 21:54
 */
public interface AccountService {
    void addAccount(String name, int money);

    int updateAccount(String name, int money);

    List<Map<String, Object>> queryAccount(String name);

}
