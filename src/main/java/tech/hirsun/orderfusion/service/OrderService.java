package tech.hirsun.orderfusion.service;

import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.pojo.PageBean;
import tech.hirsun.orderfusion.pojo.Pay;
import tech.hirsun.orderfusion.pojo.SeckillEventAction;
import tech.hirsun.orderfusion.vo.OrderVo;

public interface OrderService {

    // For Customer
    public int generalCreate(Integer loggedInUserId, Order order);

    int processSeckillRequest(Integer loggedInUserId, Order order);

    public Pay pay(Order order);

    public OrderVo getOrderVo(Integer loggedInUserId, Integer orderId);

    public Order getOrderInfo(Integer loggedInUserId, Integer orderId);

    public PageBean page(Integer pageNum, Integer pageSize, Integer userId, Integer searchId, String searchName, Integer selectStatus, Integer selectChannel);

    public SeckillEventAction seckillCreateRequest(Integer loggedInUserId, Order Order);


    // For Admin Only
    public void updateUnderAdmin(Order order);


}
