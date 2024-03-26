package tech.hirsun.orderfusion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.orderfusion.pojo.Order;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.redis.RedisService;
import tech.hirsun.orderfusion.redis.UserKey;
import tech.hirsun.orderfusion.result.ErrorMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.UserService;
import tech.hirsun.orderfusion.utils.JwtUtils;

@Slf4j
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @RequestMapping("/page")
    public String page(Model model) {
        model.addAttribute("name", "orderfusion");
        return "demo";
    }

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
    public Result<String> error() {
        return Result.error(ErrorMessage.SERVER_ERROR);
    }

//    @ResponseBody
//    @RequestMapping("/db/get")
//    public Result<User> db() {
//            User user = userService.getUserById(2);
//            return Result.success(user);
//    }

    @ResponseBody
    @RequestMapping("/redis/get/user")
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.byId,"1", User.class);
        return Result.success(user);
    }

    @ResponseBody
    @RequestMapping("/jwt")
    public Result jwt(@RequestHeader String jwt, @RequestBody Order order) {
        log.info("Request create order, order: {}, jwt: {}", order, jwt);
        JwtUtils.parseJwt(jwt);
        log.info("Logged in User id: {}", JwtUtils.parseJwt(jwt).get("id").toString());
        return Result.success(jwt);
    }

    @ResponseBody
    @RequestMapping("/redis/set/user")
    public Result<User> redisSet() {
        User user = new User();
        user.setId(1);
        user.setName("1111");
        redisService.set(UserKey.byId, "1",user);
        User userrt= redisService.get(UserKey.byId,"1", User.class);
        return Result.success(userrt);
    }



}
