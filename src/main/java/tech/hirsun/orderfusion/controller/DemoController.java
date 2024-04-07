package tech.hirsun.orderfusion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.orderfusion.dao.SeckillEventDao;
import tech.hirsun.orderfusion.dao.UserDao;
import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.redis.RedisService;
import tech.hirsun.orderfusion.redis.UserKey;
import tech.hirsun.orderfusion.result.ErrorMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.SeckillEventService;
import tech.hirsun.orderfusion.service.UserService;
import tech.hirsun.orderfusion.utils.JwtUtils;

@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private SeckillEventService seckillEventService;

    @Autowired
    private SeckillEventDao seckillEventDao;

    @ResponseBody
    @RequestMapping("/text")
    public String text() {
        return "text";
    }

    @ResponseBody
    @RequestMapping("/hello")
    public Result<String> hello() {
        return Result.success("hello, orderfusion");
    }

    @ResponseBody
    @RequestMapping("/proc/hello")
    public Result<String> procHello() {
        return Result.success("hello, logged user in orderfusion");
    }

    @ResponseBody
    @RequestMapping("/error")
    public Result error() {
        return Result.error(ErrorMessage.SERVER_ERROR);
    }

//    @ResponseBody
//    @RequestMapping("/db/get")
//    public Result<User> db() {
//            User user = userService.getUserById(2);
//            return Result.success(user);
//    }

    @ResponseBody
    @RequestMapping("/redis/get/seckill")
    public Result redisGet() {
        return Result.success(seckillEventService.getSeckillEventInfo(1));
    }

    @ResponseBody
    @RequestMapping("/db/get/seckill")
    public Result dbGet() {
        return Result.success(seckillEventDao.getSeckillEventById(1));
    }


}
