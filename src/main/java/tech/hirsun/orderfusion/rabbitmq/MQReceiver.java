package tech.hirsun.orderfusion.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.service.OrderService;
import tech.hirsun.orderfusion.utils.StringAndBeanConventer;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private OrderService orderService;

    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    public void receive(String message) {
        SeckillMessage seckillMessage = StringAndBeanConventer.stringToBean(message, SeckillMessage.class);
        log.info("receive message: " + seckillMessage);
        orderService.processSeckillRequest(seckillMessage.getUserId(), seckillMessage.getOrder());
    }


}
