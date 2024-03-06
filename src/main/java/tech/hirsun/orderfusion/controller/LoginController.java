package tech.hirsun.orderfusion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.redis.RedisService;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.UserService;

@Slf4j
@RestController
@RequestMapping("/userauth")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @RequestMapping("/login")
    public String login(@RequestBody User userAttemped){
        log.info("User login api is requested.");
        User u = userService.login(userAttemped);

        if()
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

