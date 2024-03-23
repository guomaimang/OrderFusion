package tech.hirsun.orderfusion.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.hirsun.orderfusion.pojo.Goods;
import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.pojo.Pay;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDetails {
    private Goods goods;
    private Order order;
    private Pay pay;
}
