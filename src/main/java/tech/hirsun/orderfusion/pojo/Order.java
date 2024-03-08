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

    Integer goodId;
    String goodsName;
    Integer goodsAmount;

    Integer payment;

    String deliveryAddress;
    String deliveryPhone;
    String deliveryReceiver;

    String userRemark;
    String adminRemark;

    Integer status;

    Date createTime;
    Date payTime;
    Date sentTime;

}
