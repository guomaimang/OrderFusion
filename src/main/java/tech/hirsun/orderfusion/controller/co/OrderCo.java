package tech.hirsun.orderfusion.controller.co;

import lombok.*;
import tech.hirsun.orderfusion.pojo.Order;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderCo {
    private Order order;
    private String jwt;
}
