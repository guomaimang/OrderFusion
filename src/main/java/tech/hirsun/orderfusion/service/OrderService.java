package tech.hirsun.orderfusion.service;

import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.pojo.PageBean;
import tech.hirsun.orderfusion.pojo.Pay;
import tech.hirsun.orderfusion.vo.GoodsDetails;

public interface OrderService {

    // For Customer
    public int generalCreate(Order order);

    public void update(Order order);

    public Order getOrderInfo(Integer id);

    public PageBean page(Integer pageNum, Integer pageSize, Integer userId, Integer searchId, String searchName, Integer selectStatus, Integer selectChannel);

    Pay orderPay(Integer id);

    // For Admin Only
    public void updateUnderAdmin(Order order);

    public Order getOrderInfoUnderAdmin(Integer id);

    GoodsDetails details(Integer loggedInUserId, Integer orderId);
}
