package tech.hirsun.orderfusion.service;

import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.pojo.PageBean;
import tech.hirsun.orderfusion.pojo.Pay;
import tech.hirsun.orderfusion.vo.OrderVo;

public interface OrderService {

    // For Customer
    public int generalCreate(Integer loggedInUserId, Order order);

    public void update(Order order);

    public OrderVo getOrderVo(Integer loggedInUserId, Integer orderId);

    public Order getOrderInfo(Integer loggedInUserId, Integer orderId);

    public PageBean page(Integer pageNum, Integer pageSize, Integer userId, Integer searchId, String searchName, Integer selectStatus, Integer selectChannel);

    Pay orderPay(Integer id);

    // For Admin Only
    public void updateUnderAdmin(Order order);

}
