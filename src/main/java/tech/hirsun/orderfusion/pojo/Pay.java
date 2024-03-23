package tech.hirsun.orderfusion.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class Pay {
    Integer id;
    Integer status;
    Integer method;
    String transactionId;
    Date createTime;
}
