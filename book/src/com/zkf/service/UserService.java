package com.zkf.service;

import com.zkf.pojo.User;

public interface UserService {


    /*
    * 注册用户
    * */
    public void registUser(User user);

    /*
    * 登录
    * */
    public User login(User user);

    /*
    * 检查用户名是否可用
    * */
    public boolean existsUsername(String username);

}
