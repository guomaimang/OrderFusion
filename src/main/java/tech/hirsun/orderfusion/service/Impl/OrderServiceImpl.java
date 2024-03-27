package tech.hirsun.orderfusion.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hirsun.orderfusion.dao.OrderDao;
import tech.hirsun.orderfusion.dao.SeckillOrdersetActionDao;
import tech.hirsun.orderfusion.pojo.*;
import tech.hirsun.orderfusion.rabbitmq.MQSender;
import tech.hirsun.orderfusion.rabbitmq.SeckillMessage;
import tech.hirsun.orderfusion.redis.RedisService;
import tech.hirsun.orderfusion.redis.SeckillEventActionKey;
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
    private SeckillOrdersetActionDao seckillOrdersetActionDao;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private PayService payService;

    @Autowired
    private SeckillEventService seckillEventService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MQSender mqSender;

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
        if (goodsService.minusStock(draftOrder.getGoodsId(), draftOrder.getGoodsAmount()) > 0) {
            return draftOrder.getId();
        } else {
            return -1;
        }

    }

    @Override
    public SeckillEventAction seckillCreateRequest(Integer loggedInUserId, Order order) {
        order.setUserId(loggedInUserId);
        order.setChannel(1);

        //cp the template from frontend order
        Order draftOrder = Order.getDraftObjForDB(order);
        SeckillEvent seckillEvent = seckillEventService.getSeckillEventInfo(draftOrder.getSeckillEventId());

        // check the availability
        if (seckillEvent.getIsAvailable() != 1) {
            return new SeckillEventAction(-1,null,"Sorry, this seckill event is not available.");
        }

        // check the time
        Date currentTime = new Date();
        if (seckillEvent.getStartTime().after(currentTime) || seckillEvent.getEndTime().before(currentTime)) {
            return new SeckillEventAction(-1,null,"Sorry, this seckill event is not available in this time.");
        }

        // check the limitation
        if (draftOrder.getGoodsAmount() > seckillEvent.getPurchaseLimitNum()){
            return new SeckillEventAction(-1,null,"Sorry, you have exceed the limitation of purchase.");
        }

        // check if it is perticipated
        String redisKey = "SeckillEventActionKey" +
                " userId: " + loggedInUserId +
                " seckillEventId: " + draftOrder.getSeckillEventId();
        SeckillEventAction seckillEventAction = redisService.get(SeckillEventActionKey.byParams, redisKey, SeckillEventAction.class);
        if (seckillEventAction == null){
            // means the action is creating
            // check the stock
            if (seckillEvent.getSeckillStock() < draftOrder.getGoodsAmount()){
                return new SeckillEventAction(-1,null,"Sorry, there is no enough stock in your area.");
            }
            redisService.set(SeckillEventActionKey.byParams, redisKey, new SeckillEventAction(1,null,"You are in the waiting list. Once success, you will receive the order in order list."));
            // minus the stock in redis
            seckillEvent.setSeckillStock(seckillEvent.getSeckillStock() - draftOrder.getGoodsAmount());
            // sent the request to queue
            SeckillMessage seckillMessage = new SeckillMessage(loggedInUserId, draftOrder);
            mqSender.sendSeckillMessage(seckillMessage);
        }
        // means the action is querying
        return redisService.get(SeckillEventActionKey.byParams, redisKey, SeckillEventAction.class);
    }

    @Override
    public int processSeckillRequest(Integer loggedInUserId, Order order) {
        order.setUserId(loggedInUserId);
        order.setChannel(1);

        //cp the template from frontend order
        Order draftOrder = Order.getDraftObjForDB(order);
        SeckillEvent seckillEvent = seckillEventService.getSeckillEventInfo(draftOrder.getSeckillEventId());
        String redisKey = "SeckillEventActionKey" +
                " userId: " + loggedInUserId +
                " seckillEventId: " + draftOrder.getSeckillEventId();

        // check the repeated
        if (seckillOrdersetActionDao.count(loggedInUserId, draftOrder.getSeckillEventId()) > 0 ){
            redisService.set(SeckillEventActionKey.byParams, redisKey, new SeckillEventAction(-1,null,"Sorry, you have already seckill the goods. Please check your order list."));
            return -3;
        }

        // set fields
        draftOrder.setGoodsId(seckillEvent.getGoodsId());
        draftOrder.setGoodsName(seckillEvent.getTitle());
        draftOrder.setPayment(seckillEvent.getSeckillPrice() * draftOrder.getGoodsAmount());
        draftOrder.setAdminRemark(null);
        draftOrder.setStatus(0);
        draftOrder.setCreateTime(new Date());
        draftOrder.setPayTime(null);
        draftOrder.setSentTime(null);
        draftOrder.setPayId(null);
        draftOrder.setSeckillEventId(seckillEvent.getId());

        // check update the stock
        if (seckillEventService.minusStock(draftOrder.getSeckillEventId(), draftOrder.getGoodsAmount()) > 0) {
            // insert the action into seckill_orderset_action. if duplicated, throw excaption, and the transaction will be rolled back
             seckillOrdersetActionDao.insert(loggedInUserId, draftOrder.getSeckillEventId());
            // generate the order
            orderDao.insert(draftOrder);
            // update the redis
            redisService.set(SeckillEventActionKey.byParams, redisKey, new SeckillEventAction(2,draftOrder.getId(),"Congratulations! You have successfully seckill the goods. Please check your order list."));
            return draftOrder.getId();
        } else {
            redisService.set(SeckillEventActionKey.byParams, redisKey, new SeckillEventAction(-1,null,"Sorry, there is no enough stock in your area. Please try later"));
            return -5;
        }
    }

    @Override
    @Transactional
    public Pay pay(Order order) {
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
        Goods goods = goodsService.getGoodsInfo(order.getGoodsId());
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

    // For Admin
    @Override
    public void updateUnderAdmin(Order order) {
        Order draftOrder = Order.getDraftObjForDB(order);
        orderDao.update(draftOrder);
    }

}