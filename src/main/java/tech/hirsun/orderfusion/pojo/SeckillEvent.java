package tech.hirsun.orderfusion.pojo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class SeckillEvent {
    int id;
    int goodsId;
    double seckillPrice;
    int secKillStock;

    Date startTime;
    Date endTime;

    int isAvailable;
}
