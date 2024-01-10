package tech.hirsun.orderfusion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.hirsun.orderfusion.redis.RedisService;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.UserService;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/login_check")
    public Result<Boolean> loginCheck() {
        return null;
    }


    @RequestMapping("/login_action")
    @ResponseBody
    public String loginAction() {
        return "loginAction";
    }



}

