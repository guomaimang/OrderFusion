package tech.hirsun.orderfusion.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    int id;
    int userId;

    int goodId;
    String goodsName;
    int goodsAmount;

    double payment;

    String deliveryAddress;
    String deliveryPhone;
    String deliveryReceiver;

    String userRemark;
    String adminRemark;

    int status;

    Date createTime;
    Date payTime;
    Date sentTime;

}
