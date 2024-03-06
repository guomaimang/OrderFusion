package tech.hirsun.orderfusion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.redis.RedisService;
import tech.hirsun.orderfusion.result.CodeMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.UserService;
import tech.hirsun.orderfusion.utils.JwtUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/userauth")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @RequestMapping("/login")
    public Result login(@RequestBody User userAttemped){
        log.info("User login api is requested.");
        User u = userService.login(userAttemped);

        // If user exists, create a JWT and return it
        if(u != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("email", u.getEmail());

            String jwt = JwtUtils.createJWT(claims);
            return Result.success(jwt);
        }else {
            // If user does not exist, return error message
            return Result.error(CodeMessage.USER_NOT_EXIST);
        }

    }


}

