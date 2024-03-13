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
            if (order.getGoodsName().length() > 0){
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
            if (order.getDeliveryAddress().length() > 0) {
                draftOrder.setDeliveryAddress(order.getDeliveryAddress());
            }
        }

        if (order.getDeliveryPhone() != null) {
            if (order.getDeliveryPhone().length() > 0) {
                draftOrder.setDeliveryPhone(order.getDeliveryPhone());
            }
        }

        if (order.getDeliveryReceiver() != null) {
            if (order.getDeliveryReceiver().length() > 0) {
                draftOrder.setDeliveryReceiver(order.getDeliveryReceiver());
            }
        }

        if (order.getUserRemark() != null) {
            if (order.getUserRemark().length() > 0) {
                draftOrder.setUserRemark(order.getUserRemark());
            }
        }

        if (order.getAdminRemark() != null) {
            if (order.getAdminRemark().length() > 0) {
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

        return draftOrder;
    }

}
