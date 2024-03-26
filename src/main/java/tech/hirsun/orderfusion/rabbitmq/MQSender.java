package tech.hirsun.orderfusion.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hirsun.orderfusion.redis.RedisService;
import tech.hirsun.orderfusion.utils.StringAndBeanConventer;

@Service
public class MQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendSeckillMessage(SeckillMessage message) {
        String msg = StringAndBeanConventer.beanToString(message);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE, msg);
    }

}
