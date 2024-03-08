package tech.hirsun.orderfusion.pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class SeckillEvent {
    Integer id;
    Integer goodsId;
    Double seckillPrice;
    Integer secKillStock;

    Date startTime;
    Date endTime;

    Integer isAvailable;
}
