package tech.hirsun.orderfusion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.result.ErrorMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.OrderService;
import tech.hirsun.orderfusion.utils.JwtUtils;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // General means general shopping and not seckill
    @PostMapping("/general/create")
    public Result create(@RequestHeader String jwt, @RequestBody Order order) {
        try {
            log.info("Request create order, order: {}, jwt: {}", order, jwt);
            int loggedInUserId = Integer.parseInt(JwtUtils.parseJwt(jwt).get("id").toString());
            order.setUserId(loggedInUserId);
            order.setChannel(0);

            int orderId = orderService.generalCreate(order);
            if (orderId > 0) {
                return Result.success(orderId);
            } else {
                return Result.error(ErrorMessage.ORDER_NO_PERMISSION_GENERATION);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error when user request create order");
            return Result.error(new ErrorMessage(50000, "Order failed, no payment will be deducted. Please try again."));
        }
    }

    // General means general shopping and not seckill
    @PutMapping("/general/update")
    public Result update(@RequestHeader String jwt, @RequestBody Order order) {
        try {
            log.info("Request update order, order: {}, jwt: {}", order, jwt);
            int loggedInUserId = Integer.parseInt(JwtUtils.parseJwt(jwt).get("id").toString());
            log.info("Logged in User id: {}", loggedInUserId);

            if (loggedInUserId != order.getUserId()) {
                return Result.error(ErrorMessage.USER_NO_PERMISSION);
            }else {
                orderService.update(order);
                return Result.success();
            }

        } catch (Exception e) {
            log.error("Error when user request update order");
            return Result.error(new ErrorMessage(50000, "Update failed, please try again."));
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
                return Result.error(ErrorMessage.USER_NO_PERMISSION);
            }else {
                return Result.success(order);
            }

        } catch (Exception e) {
            log.error("Error when user request update order");
            return Result.error(new ErrorMessage(50000, "Update failed, please try again."));
        }
    }

    @GetMapping("/list")
    public Result list(@RequestHeader String jwt,
                       @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                       @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                       @RequestParam(name = "searchName", required = false) String searchName,
                       @RequestParam(name = "selectStatus", required = false) Integer selectStatus,
                       @RequestParam(name = "selectChannel", required = false) Integer selectChannel) {
        try {
            log.info("Request order list, pageNum: {}, pageSize: {}, searchName: {}, selectStatus: {}, selectChannel: {}", pageNum, pageSize, searchName, selectStatus, selectChannel);
            int loggedInUserId = Integer.parseInt(JwtUtils.parseJwt(jwt).get("id").toString());
            log.info("Logged in User id: {}", loggedInUserId);
            return Result.success(orderService.page(pageNum, pageSize, loggedInUserId, null, searchName, selectStatus, selectChannel));
        } catch (Exception e) {
            log.error("Error when user request order list");
            return Result.error(new ErrorMessage(50000, "Request failed, please try again."));
        }
    }

    @GetMapping("/details/{id}")
    public Result details(@RequestHeader String jwt,
                          @PathVariable("id") Integer goodsId) {
        try {
            log.info("Request order details, id: {}, jwt: {}", goodsId, jwt);
            int loggedInUserId = Integer.parseInt(JwtUtils.parseJwt(jwt).get("id").toString());
            log.info("Logged in User id: {}", loggedInUserId);
            return Result.success(orderService.details(loggedInUserId, goodsId));
        } catch (Exception e) {
            log.error("Error when user request order details");
            return Result.error(new ErrorMessage(50000, "Request failed, please try again."));
        }
    }

}