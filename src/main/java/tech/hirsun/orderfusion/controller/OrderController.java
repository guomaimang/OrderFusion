package tech.hirsun.orderfusion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.result.CodeMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.OrderService;
import tech.hirsun.orderfusion.utils.JwtUtils;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PutMapping("/general/create")
    public Result create(@RequestHeader String jwt, @RequestBody Order order) {
        try {
            log.info("Request create order, order: {}, jwt: {}", order, jwt);
            int loggedInUserId = Integer.parseInt(JwtUtils.parseJwt(jwt).get("id").toString());
            log.info("Logged in User id: {}", loggedInUserId);
            order.setChannel(0);

            if (loggedInUserId != order.getUserId()) {
                return Result.error(CodeMessage.USER_NO_PERMISSION);
            }else {
                int orderId = orderService.create(order);
                if (orderId > 0) {
                    return Result.success(orderId);
                } else {
                    return Result.error(new CodeMessage(50000, "Order failed, no payment will be deducted."));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error when user request create order");
            return Result.error(new CodeMessage(50000, "Order failed, no payment will be deducted. Please try again."));
        }
    }

    @PostMapping("/general/update")
    public Result update(@RequestHeader String jwt, @RequestBody Order order) {
        try {
            log.info("Request update order, order: {}, jwt: {}", order, jwt);
            int loggedInUserId = Integer.parseInt(JwtUtils.parseJwt(jwt).get("id").toString());
            log.info("Logged in User id: {}", loggedInUserId);

            if (loggedInUserId != order.getUserId()) {
                return Result.error(CodeMessage.USER_NO_PERMISSION);
            }else {
                orderService.update(order);
                return Result.success(CodeMessage.SUCCESS);
            }

        } catch (Exception e) {
            log.error("Error when user request update order");
            return Result.error(new CodeMessage(50000, "Update failed, please try again."));
        }
    }

    @GetMapping("/info/{id}")
    public Result getOrderInfo(@RequestHeader String jwt, @PathVariable("id") Integer id) {
        try {
            log.info("Request order info, id: {}, jwt: {}", id, jwt);
            int loggedInUserId = Integer.parseInt(JwtUtils.parseJwt(jwt).get("id").toString());
            log.info("Logged in User id: {}", loggedInUserId);

            Order order = orderService.getOrderInfo(id);

            if (loggedInUserId != order.getUserId()) {
                return Result.error(CodeMessage.USER_NO_PERMISSION);
            }else {
                return Result.success(order);
            }

        } catch (Exception e) {
            log.error("Error when user request update order");
            return Result.error(new CodeMessage(50000, "Update failed, please try again."));
        }
    }

    @GetMapping("/list")
    public Result list(@RequestHeader String jwt,
                            @RequestParam("pageNum") Integer pageNum,
                            @RequestParam("pageSize") Integer pageSize,
                            @RequestParam("keyword") String keyword) {
        try {
            log.info("Request order list, pageNum: {}, pageSize: {}, keyword: {}, jwt: {}", pageNum, pageSize, keyword, jwt);
            int loggedInUserId = Integer.parseInt(JwtUtils.parseJwt(jwt).get("id").toString());
            log.info("Logged in User id: {}", loggedInUserId);
            return Result.success(orderService.page(pageNum, pageSize, keyword, String.valueOf(loggedInUserId)));
        } catch (Exception e) {
            log.error("Error when user request order list");
            return Result.error(new CodeMessage(50000, "Request failed, please try again."));
        }
    }

}