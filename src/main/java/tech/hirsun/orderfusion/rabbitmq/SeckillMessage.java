package tech.hirsun.orderfusion.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import tech.hirsun.orderfusion.pojo.Order;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeckillMessage {
    private Integer userId;
    private Order order;
}
