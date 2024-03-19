package tech.hirsun.orderfusion.service.Impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.hirsun.orderfusion.dao.GoodsDao;
import tech.hirsun.orderfusion.dao.OrderDao;
import tech.hirsun.orderfusion.pojo.Goods;
import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.pojo.PageBean;
import tech.hirsun.orderfusion.service.GoodsService;
import tech.hirsun.orderfusion.service.OrderService;

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

    // For Customer
    /**
     * Create a new order (non seckill)
     * @param order
     * @return
     */
    @Override
    @Transactional
    public int generalCreate(Order order) {
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
        if (goodsDao.generalMinusStock(draftOrder.getGoodsId(), draftOrder.getGoodsAmount()) > 0) {
            return draftOrder.getId();
        } else {
            return -1;
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
        draftOrder.setDeliveryAddress(null);
        draftOrder.setDeliveryPhone(null);
        draftOrder.setDeliveryReceiver(null);
        draftOrder.setAdminRemark(null);
        draftOrder.setStatus(null);
        draftOrder.setCreateTime(null);
        draftOrder.setPayTime(null);
        draftOrder.setSentTime(null);
        draftOrder.setChannel(null);

        orderDao.update(draftOrder);
    }

    @Override
    public Order getOrderInfo(Integer id) {
        return orderDao.getOrderById(id);
    }

    @Override
    public PageBean page(Integer pageNum, Integer pageSize, String keyword, String userId) {
        int count = orderDao.count(keyword, Integer.parseInt(userId));

        int start = (pageNum-1) * pageSize;
        List<Order> orders = orderDao.list(start, pageSize, keyword, Integer.parseInt(userId));
        log.info(start + " " + pageSize + " " + keyword + " " + Integer.parseInt(userId));

        return new PageBean(count, orders,Math.floorDiv(count, pageSize) + 1, pageNum);
    }

    @Override
    public void orderPay(Integer id) {
    }

    // For Admin
    @Override
    public void updateUnderAdmin(Order order) {
        Order draftOrder = Order.getDraftObjForDB(order);
        orderDao.update(draftOrder);
    }

    @Override
    public Order getOrderInfoUnderAdmin(Integer id) {
        return orderDao.getOrderById(id);
    }

}