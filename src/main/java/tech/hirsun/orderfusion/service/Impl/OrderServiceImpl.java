package tech.hirsun.orderfusion.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hirsun.orderfusion.dao.GoodsDao;
import tech.hirsun.orderfusion.dao.OrderDao;
import tech.hirsun.orderfusion.dao.SeckillEventDao;
import tech.hirsun.orderfusion.pojo.*;
import tech.hirsun.orderfusion.service.*;
import tech.hirsun.orderfusion.vo.OrderVo;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private PayService payService;

    @Autowired
    private SeckillEventService seckillEventService;

    @Autowired
    private SeckillEventDao seckillEventDao;


    // For Customer
    /**
     * Create a new order (non seckill)
     */
    @Override
    @Transactional
    public int generalCreate(Integer loggedInUserId, Order order) {
        order.setUserId(loggedInUserId);
        order.setChannel(0);

        //cp the template from frontend order
        Order draftOrder = Order.getDraftObjForDB(order);
        Goods goods = goodsService.getGoodsInfo(draftOrder.getGoodsId());

        // check the availability
        if (goods.getIsAvailable() != 1) {
            return -1;
        }

        // check the stock
        if (goods.getStock() < draftOrder.getGoodsAmount()){
            return -1;
        }

        // set fields
        draftOrder.setGoodsName(goods.getName());
        draftOrder.setPayment(goods.getPrice() * draftOrder.getGoodsAmount());
        draftOrder.setAdminRemark(null);
        draftOrder.setStatus(0);
        draftOrder.setCreateTime(new Date());
        draftOrder.setPayTime(null);
        draftOrder.setSentTime(null);
        draftOrder.setPayId(null);

        orderDao.insert(draftOrder);

        // update the stock
        if (goodsDao.minusStock(draftOrder.getGoodsId(), draftOrder.getGoodsAmount()) > 0) {
            return draftOrder.getId();
        } else {
            return -1;
        }

    }

    @Override
    @Transactional
    public int seckillCreate(Integer loggedInUserId, Order order) {
        order.setUserId(loggedInUserId);
        order.setChannel(1);

        //cp the template from frontend order
        Order draftOrder = Order.getDraftObjForDB(order);
        SeckillEvent seckillEvent = seckillEventService.getSeckillEventInfo(draftOrder.getSeckillEventId());

        // check the availability
        if (seckillEvent.getIsAvailable() != 1) {
            return -1;
        }

        // check the time
        Date currentTime = new Date();
        if (seckillEvent.getStartTime().after(currentTime) || seckillEvent.getEndTime().before(currentTime)) {
            return -1;
        }

        // check the stock
        if (seckillEvent.getSeckillStock() < draftOrder.getGoodsAmount()){
            return -2;
        }

        // check the repeated
        if (orderDao.countOnesSeckill(loggedInUserId, draftOrder.getSeckillEventId()) > 0 ){
            return -3;
        }

        // check the limitation
        if (draftOrder.getGoodsAmount() > seckillEvent.getPurchaseLimitNum()){
            return -4;
        }

        // set fields
        draftOrder.setGoodsId(seckillEvent.getGoodsId());
        draftOrder.setGoodsName(seckillEvent.getTitle());
        draftOrder.setPayment(seckillEvent.getSeckillPrice() * draftOrder.getGoodsAmount());
        draftOrder.setAdminRemark(null);
        draftOrder.setStatus(0);
        draftOrder.setCreateTime(currentTime);
        draftOrder.setPayTime(null);
        draftOrder.setSentTime(null);
        draftOrder.setPayId(null);
        draftOrder.setSeckillEventId(seckillEvent.getId());

        // update the stock
        if (seckillEventDao.minusStock(draftOrder.getSeckillEventId(), draftOrder.getGoodsAmount()) > 0) {
            // generate the order
            orderDao.insert(draftOrder);
            return draftOrder.getId();
        } else {
            return -5;
        }

    }

    @Override
    public void update(Order order) {
        Order draftOrder = Order.getDraftObjForDB(order);

        //set non-editable fields
        draftOrder.setUserId(null);
        draftOrder.setGoodsId(null);
        draftOrder.setGoodsName(null);
        draftOrder.setGoodsAmount(null);
        draftOrder.setPayment(null);
        draftOrder.setAdminRemark(null);
        draftOrder.setStatus(null);
        draftOrder.setCreateTime(null);
        draftOrder.setPayTime(null);
        draftOrder.setSentTime(null);
        draftOrder.setChannel(null);

        orderDao.update(draftOrder);
    }

    /**
     * Get the order view for frontend
     * @param loggedInUserId: if = 0, release directly
     */
    @Override
    public OrderVo getOrderVo(Integer loggedInUserId, Integer orderId) {
        Order order = orderDao.getOrderById(orderId);
        if(order == null){
            return null;
        }
        if (loggedInUserId != order.getUserId() && loggedInUserId != 0){
            return null;
        }
        Goods goods = goodsDao.getGoodsById(order.getGoodsId());
        if (goods == null) {
            return null;
        }

        Pay pay = null;
        SeckillEvent seckillEvent = null;

        if (order.getPayId() != null) {
            pay = payService.getPayInfo(order.getPayId());
        }
        if (order.getSeckillEventId() != null) {
            seckillEvent = seckillEventService.getSeckillEventInfo(order.getSeckillEventId());
        }
        return new OrderVo(goods, order, pay, seckillEvent);
    }

    /**
     * Get the order view for frontend
     * @param loggedInUserId: if = 0, release directly
     */
    @Override
    public Order getOrderInfo(Integer loggedInUserId, Integer orderId) {
        Order order = orderDao.getOrderById(orderId);
        if(order == null){
            return null;
        }
        if (loggedInUserId != order.getUserId() && loggedInUserId != 0){
            return null;
        }
        return order;
    }

    @Override
    public PageBean page(Integer pageNum, Integer pageSize, Integer userId, Integer searchId, String searchName, Integer selectStatus, Integer selectChannel) {
        int count = orderDao.count(
                userId,
                searchId,
                searchName,
                selectStatus,
                selectChannel
        );

        int start = (pageNum-1) * pageSize;
        List<Order> orders = orderDao.list(
                start,
                pageSize,
                userId,
                searchId,
                searchName,
                selectStatus,
                selectChannel
        );

        return new PageBean(count, orders,Math.floorDiv(count, pageSize) + 1, pageNum);
    }

    @Transactional
    @Override
    public Pay orderPay(Integer id) {
        Order order = orderDao.getOrderById(id);
        if (order == null || order.getStatus() != 0) {
            return null;
        }
        if (order.getDeliveryAddress() == null || order.getDeliveryPhone() == null || order.getDeliveryReceiver() == null ||
                order.getDeliveryAddress().isEmpty() || order.getDeliveryPhone().isEmpty() || order.getDeliveryReceiver().isEmpty()) {
            return null;
        }

        Pay pay = payService.virtualPay();
        order.setPayId(pay.getId());
        order.setPayTime(pay.getPayTime());
        order.setStatus(1);
        orderDao.update(order);
        return pay;
    }

    // For Admin
    @Override
    public void updateUnderAdmin(Order order) {
        Order draftOrder = Order.getDraftObjForDB(order);
        orderDao.update(draftOrder);
    }

}