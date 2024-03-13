package tech.hirsun.orderfusion.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.result.CodeMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.OrderService;

@Slf4j
@RestController
@RequestMapping("/order/general/create")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Result createOrder(@RequestBody Order order) {
        try {
            log.info("Request create order, order: {}", order);
            return Result.success(orderService.create(order));
        } catch (Exception e) {
            log.error("Error when user request create order");
            return Result.error(new CodeMessage(50000, "Order failed, no payment will be deducted. Please try again."));
        }
    }
}