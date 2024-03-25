package tech.hirsun.orderfusion.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    Integer id;
    Integer userId;

    Integer goodsId;
    String goodsName;
    Integer goodsAmount;

    Double payment;

    String deliveryAddress;
    String deliveryPhone;
    String deliveryReceiver;

    String userRemark;
    String adminRemark;

    Integer status;

    Date createTime;
    Date payTime;
    Date sentTime;

    Integer channel;
    Integer payId;
    Integer seckillEventId;

    public static Order getDraftObjForDB(Order order){
        Order draftOrder = new Order();

        if (order.getId() != null){
            draftOrder.setId(order.getId());
        }

        if (order.getUserId() != null){
            draftOrder.setUserId(order.getUserId());
        }

        if (order.getGoodsId() != null){
            draftOrder.setGoodsId(order.getGoodsId());
        }

        if (order.getGoodsName() != null){
            if (!order.getGoodsName().isEmpty()){
                draftOrder.setGoodsName(order.getGoodsName());
            }
        }

        if (order.getGoodsAmount() != null){
            draftOrder.setGoodsAmount(order.getGoodsAmount());
        }

        if (order.getPayment() != null){
            draftOrder.setPayment(order.getPayment());
        }

        if (order.getDeliveryAddress() != null) {
            if (!order.getDeliveryAddress().isEmpty()) {
                draftOrder.setDeliveryAddress(order.getDeliveryAddress());
            }
        }

        if (order.getDeliveryPhone() != null) {
            if (!order.getDeliveryPhone().isEmpty()) {
                draftOrder.setDeliveryPhone(order.getDeliveryPhone());
            }
        }

        if (order.getDeliveryReceiver() != null) {
            if (!order.getDeliveryReceiver().isEmpty()) {
                draftOrder.setDeliveryReceiver(order.getDeliveryReceiver());
            }
        }

        if (order.getUserRemark() != null) {
            if (!order.getUserRemark().isEmpty()) {
                draftOrder.setUserRemark(order.getUserRemark());
            }
        }

        if (order.getAdminRemark() != null) {
            if (!order.getAdminRemark().isEmpty()) {
                draftOrder.setAdminRemark(order.getAdminRemark());
            }
        }

        if (order.getStatus() != null) {
            draftOrder.setStatus(order.getStatus());
        }

        if (order.getPayTime() != null) {
            draftOrder.setPayTime(order.getPayTime());
        }

        if (order.getSentTime() != null) {
            draftOrder.setSentTime(order.getSentTime());
        }

        if (order.getChannel() != null) {
            draftOrder.setChannel(order.getChannel());
        }

        if (order.getPayId() != null){
            draftOrder.setPayId(order.getPayId());
        }
        if (order.getSeckillEventId() != null){
            draftOrder.setSeckillEventId(order.getSeckillEventId());
        }

        return draftOrder;
    }

}
