package tech.hirsun.orderfusion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.orderfusion.pojo.Goods;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.result.CodeMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.GoodsService;
import tech.hirsun.orderfusion.service.UserService;


@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;


    @GetMapping("/user/list")
    public Result page(@RequestParam(defaultValue = "1") Integer pagenum,
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
    public Result add(@RequestBody User user) {
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
    public Result lockSwitch(@RequestBody User user) {
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
    public Result edit(@RequestBody User user) {
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
}
