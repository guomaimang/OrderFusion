package tech.hirsun.orderfusion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.orderfusion.pojo.Goods;
import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.result.CodeMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.GoodsService;
import tech.hirsun.orderfusion.service.OrderService;
import tech.hirsun.orderfusion.service.UserService;


@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;


    // For User
    @GetMapping("/user/list")
    public Result pageUser(@RequestParam(defaultValue = "1") Integer pagenum,
                       @RequestParam(defaultValue = "10") Integer pagesize,
                       String keyword) {
        try {
            log.info("Admin request user list, pageNum: {}, pageSize: {}, keyword: {}", pagenum, pagesize, keyword);
            return Result.success(userService.page(pagenum, pagesize, keyword));
        } catch (Exception e) {
            log.error("Error when admin request user list");
            return Result.error(new CodeMessage(50000, "Illegal Request"));
        }
    }

    @PostMapping("/user/add")
    public Result addUser(@RequestBody User user) {
        try {
            log.info("Admin request add user, name: {}, email: {}", user.getName(), user.getEmail());
            userService.add(new User(user.getName(), user.getEmail(), user.getPassword()));
            return Result.success("Success");
        } catch (Exception e) {
            log.error("Error when add user");
            return Result.error(new CodeMessage(50000, "Illegal Request"));
        }
    }

    @PutMapping("/user/lockswitch")
    public Result lockSwitchUser(@RequestBody User user) {
        try {
            log.info("Admin request lock switch, id: {}", user.getId());
            userService.lockSwitch(user);
            return Result.success("Success");
        } catch (Exception e) {
            log.error("Error when lock switch");
            return Result.error(new CodeMessage(50000, "Illegal Request"));
        }
    }

    @PutMapping("/user/edit")
    public Result editUser(@RequestBody User user) {
        try {
            log.info("Admin request edit user, id: {}", user.getId());
            userService.update(user);
            return Result.success("Success");
        } catch (Exception e) {
            log.error("Error when edit user");
            return Result.error(new CodeMessage(50000, "Illegal Request"));
        }
    }

    @GetMapping("/user/info")
    public Result getUserInfo(@RequestParam Integer id) {
        log.info("Admin request user info, id: {}", id);
        try {
            return Result.success(userService.getUserInfo(id));
        } catch (Exception e) {
            log.error("Error when get user info");
            return Result.error(new CodeMessage(50000, "Illegal Request"));
        }
    }

    // For Goods

    @PostMapping("/goods/add")
    public Result addGoods(@RequestBody Goods goods) {
        try {
            log.info("Admin request add goods, name: {}", goods.getName());
            goodsService.add(goods);
            return Result.success("Success");
        } catch (Exception e) {
            log.error("Error when add goods");
            return Result.error(new CodeMessage(50000, "Illegal Request"));
        }
    }

    @PutMapping("/goods/edit")
    public Result editGoods(@RequestBody Goods goods) {
        try {
            log.info("Admin request edit goods, id: {}", goods.getId());
            goodsService.update(goods);
            return Result.success("Success");
        } catch (Exception e) {
            log.error("Error when edit goods");
            return Result.error(new CodeMessage(50000, "Illegal Request"));
        }
    }

    // For Order
    @PutMapping("/order/update")
    public Result orderUpdate(@RequestBody Order order) {
        try {
            log.info("Request update order under admin, order: {}", order);
            orderService.updateUnderAdmin(order);
            return Result.success("Order updated successfully");
        } catch (Exception e) {
            log.error("Error when admin request update order");
            return Result.error(new CodeMessage(50000, "Update failed, please try again."));
        }
    }

    @GetMapping("/order/info/{id}")
    public Result infoOrder(@PathVariable("id") Integer id){
        try {
            log.info("Request order info under admin, id: {}", id);
            return Result.success(orderService.getOrderInfoUnderAdmin(id));
        } catch (Exception e) {
            log.error("Error when admin request order info");
            return Result.error(new CodeMessage(50000, "Illegal Request"));
        }
    }

    @GetMapping("/order/list")
    public Result list(@RequestParam("pageNum") Integer pageNum,
                       @RequestParam("pageSize") Integer pageSize,
                       @RequestParam("keyword") String keyword,
                       @RequestParam("userid") String userId){
        try {
            log.info("Request order list under admin, pageNum: {}, pageSize: {}, keyword: {}, userId: {}", pageNum, pageSize, keyword, userId);
            return Result.success(orderService.page(pageNum, pageSize, keyword, userId));
        } catch (Exception e) {
            log.error("Error when user request order list");
            return Result.error(new CodeMessage(50000, "Request failed, please try again."));
        }
    }


}
