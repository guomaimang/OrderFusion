package tech.hirsun.orderfusion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.hirsun.orderfusion.model.User;
import tech.hirsun.orderfusion.redis.RedisService;
import tech.hirsun.orderfusion.redis.UserKey;
import tech.hirsun.orderfusion.result.CodeMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.UserService;

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
    @RequestMapping("/error")
    public Result<String> error() {
        return Result.error(CodeMessage.SERVER_ERROR);
    }

    @ResponseBody
    @RequestMapping("/db/get")
    public Result<User> db() {
            User user = userService.getUserById(2);
            return Result.success(user);
    }

    @ResponseBody
    @RequestMapping("/redis/get/user")
    public Result<User> redisGet() {
        User user = redisService.get(UserKey.getById,"1", User.class);
        return Result.success(user);
    }

    @ResponseBody
    @RequestMapping("/redis/set/user")
    public Result<User> redisSet() {
        User user = new User(1,"hanjiaming");
        redisService.set(UserKey.getById, "1",user);
        User userrt= redisService.get(UserKey.getById,"1", User.class);
        return Result.success(userrt);
    }



}
