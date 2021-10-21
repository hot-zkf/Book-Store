package com.zkf.service;

import com.zkf.pojo.Cart;

public interface OrderService {

    public String createOrder(Cart cart,Integer userId);
}
