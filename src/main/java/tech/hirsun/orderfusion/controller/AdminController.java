package tech.hirsun.orderfusion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.orderfusion.pojo.Goods;
import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.pojo.SeckillEvent;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.result.ErrorMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.GoodsService;
import tech.hirsun.orderfusion.service.OrderService;
import tech.hirsun.orderfusion.service.SeckillEventService;
import tech.hirsun.orderfusion.service.UserService;
import tech.hirsun.orderfusion.vo.OrderVo;


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
    @Autowired
    private SeckillEventService seckillEventService;


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
            return Result.error(new ErrorMessage(50000, "Illegal Request"));
        }
    }

    @PostMapping("/user/add")
    public Result addUser(@RequestBody User user) {
        try {
            log.info("Admin request add user, name: {}, email: {}", user.getName(), user.getEmail());
            userService.add(new User(user.getName(), user.getEmail(), user.getPassword()));
            return Result.success();
        } catch (Exception e) {
            log.error("Error when add user");
            return Result.error(new ErrorMessage(50000, "Illegal Request"));
        }
    }

    @PutMapping("/user/lockswitch")
    public Result lockSwitchUser(@RequestBody User user) {
        try {
            log.info("Admin request lock switch, id: {}", user.getId());
            userService.lockSwitch(user);
            return Result.success();
        } catch (Exception e) {
            log.error("Error when lock switch");
            return Result.error(new ErrorMessage(50000, "Illegal Request"));
        }
    }

    @PutMapping("/user/edit")
    public Result editUser(@RequestBody User user) {
        try {
            log.info("Admin request edit user, id: {}", user.getId());
            userService.update(user);
            return Result.success();
        } catch (Exception e) {
            log.error("Error when edit user");
            return Result.error(new ErrorMessage(50000, "Illegal Request"));
        }
    }

    @GetMapping("/user/info")
    public Result getUserInfo(@RequestParam Integer id) {
        log.info("Admin request user info, id: {}", id);
        try {
            return Result.success(userService.getUserInfo(id));
        } catch (Exception e) {
            log.error("Error when get user info");
            return Result.error(new ErrorMessage(50000, "Illegal Request"));
        }
    }

    // For Goods
    @PostMapping("/goods/add")
    public Result addGoods(@RequestBody Goods goods) {
        try {
            log.info("Admin request add goods, name: {}", goods.getName());
            goodsService.add(goods);
            return Result.success();
        } catch (Exception e) {
            log.error("Error when add goods");
            return Result.error(new ErrorMessage(50000, "Illegal Request"));
        }
    }

    @PutMapping("/goods/edit")
    public Result editGoods(@RequestBody Goods goods) {
        try {
            log.info("Admin request edit goods, id: {}", goods.getId());
            goodsService.update(goods);
            return Result.success();
        } catch (Exception e) {
            log.error("Error when edit goods");
            return Result.error(new ErrorMessage(50000, "Illegal Request"));
        }
    }

    // For SeckillEcent
    @PostMapping("/seckill/add")
    public Result addSeckill(@RequestBody SeckillEvent seckillEvent) {
        try {
            log.info("Admin request add seckill, title: {}", seckillEvent.getTitle());
            seckillEventService.create(seckillEvent);
            return Result.success();
        } catch (Exception e) {
            log.error("Error when add seckill");
            return Result.error(new ErrorMessage(50000, "Illegal Request"));
        }
    }

    @PutMapping("/seckill/edit")
    public Result editSeckill(@RequestBody SeckillEvent seckillEvent) {
        try {
            log.info("Admin request edit seckill, id: {}", seckillEvent.getId());
            seckillEventService.update(seckillEvent);
            return Result.success();
        } catch (Exception e) {
            log.error("Error when edit seckill");
            return Result.error(new ErrorMessage(50000, "Illegal Request"));
        }
    }

    // For Order
    @PutMapping("/order/update")
    public Result orderUpdate(@RequestBody Order order) {
        try {
            log.info("Request update order under admin, order: {}", order);
            orderService.updateUnderAdmin(order);
            return Result.success();
        } catch (Exception e) {
            log.error("Error when admin request update order");
            return Result.error(new ErrorMessage(50000, "Update failed, please try again."));
        }
    }

    @GetMapping("/order/info/{id}")
    public Result infoOrder(@PathVariable("id") Integer orderId){
        try {
            log.info("Admin request order details, id: {}", orderId);
            OrderVo OrderVo = orderService.getOrderVo(0, orderId);
            if (OrderVo == null) {
                return Result.error(ErrorMessage.USER_NO_PERMISSION);
            }else{
                return Result.success(OrderVo);
            }
        } catch (Exception e) {
            log.error("Error when user request order details");
            return Result.error(new ErrorMessage(50000, "Request failed, please try again."));
        }
    }

    @GetMapping("/order/list")
    public Result listOrder(
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
            @RequestParam(name = "searchId",required = false) Integer searchId,
            @RequestParam(name = "searchName", required = false) String searchName,
            @RequestParam(name = "selectStatus", required = false) Integer selectStatus,
            @RequestParam(name = "selectChannel", required = false) Integer selectChannel){
        try {
            log.info("Request order list under admin, pageNum: {}, pageSize: {}, searchId: {}, searchName: {}, selectStatus: {}, selectChannel: {}", pageNum, pageSize, searchId, searchName, selectStatus, selectChannel);
            return Result.success(orderService.page(pageNum, pageSize,null, searchId ,searchName, selectStatus, selectChannel));
        } catch (Exception e) {
            log.error("Error when user request order list");
            return Result.error(new ErrorMessage(50000, "Request failed, please try again."));
        }

    }
}
