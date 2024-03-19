package tech.hirsun.orderfusion.service;

import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.pojo.PageBean;

public interface OrderService {

    // For Customer
    public int generalCreate(Order order);

    public void update(Order order);

    public Order getOrderInfo(Integer id);

    public PageBean page(Integer pagenum, Integer pagesize, String keyword, String userid);

    void orderPay(Integer id);

    // For Admin Only
    public void updateUnderAdmin(Order order);

    public Order getOrderInfoUnderAdmin(Integer id);
}
