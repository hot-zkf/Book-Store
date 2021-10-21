package com.zkf.dao.impl;

import com.zkf.dao.OrderDao;
import com.zkf.pojo.Order;

public class OrderDaoImpl extends BaseDao implements OrderDao {
    @Override
    public int saveOrder(Order order) {

        System.out.println("OrderDaoImpl 程序在[" + Thread.currentThread().getName()+"]中");


        String sql = "insert into t_order(`order_id`,`create_time`,`price`,`status`,`user_id`) values(?,?,?,?,?)";

        return update(sql,order.getOrderId(),order.getCreateTime(),order.getPrice(),order.getStatus(),order.getUserId());

    }
}
