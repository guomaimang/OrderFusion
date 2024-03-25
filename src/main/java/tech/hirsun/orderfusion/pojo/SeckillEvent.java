package tech.hirsun.orderfusion.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeckillEvent {
    Integer id;
    Integer goodsId;
    String Title;
    Double seckillPrice;
    Integer seckillStock;

    Date startTime;
    Date endTime;

    Integer isAvailable;
    Integer purchaseLimitNum;

    public static SeckillEvent getDraftObjForDB(SeckillEvent seckillEvent){
        SeckillEvent draftSeckillEvent = new SeckillEvent();

        if (seckillEvent.getId() != null){
            draftSeckillEvent.setId(seckillEvent.getId());
        }

        if (seckillEvent.getGoodsId() != null){
            draftSeckillEvent.setGoodsId(seckillEvent.getGoodsId());
        }

        if (seckillEvent.getTitle() != null){
            if (!seckillEvent.getTitle().isEmpty()){
                draftSeckillEvent.setTitle(seckillEvent.getTitle());
            }
        }

        if (seckillEvent.getSeckillPrice() != null){
            draftSeckillEvent.setSeckillPrice(seckillEvent.getSeckillPrice());
        }

        if (seckillEvent.getSeckillStock() != null){
            draftSeckillEvent.setSeckillStock(seckillEvent.getSeckillStock());
        }

        if (seckillEvent.getStartTime() != null){
            draftSeckillEvent.setStartTime(seckillEvent.getStartTime());
        }

        if (seckillEvent.getEndTime() != null){
            draftSeckillEvent.setEndTime(seckillEvent.getEndTime());
        }

        if (seckillEvent.getIsAvailable() != null){
            draftSeckillEvent.setIsAvailable(seckillEvent.getIsAvailable());
        }

        if (seckillEvent.getPurchaseLimitNum() != null){
            draftSeckillEvent.setPurchaseLimitNum(seckillEvent.getPurchaseLimitNum());
        }

        return draftSeckillEvent;
    }
}
