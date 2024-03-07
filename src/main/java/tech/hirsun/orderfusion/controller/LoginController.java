package tech.hirsun.orderfusion.controller;

import com.alibaba.fastjson2.JSON;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.redis.RedisService;
import tech.hirsun.orderfusion.result.CodeMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.UserService;
import tech.hirsun.orderfusion.utils.JwtUtils;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/userauth")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @PostMapping("/login")
    public Result login(@RequestBody User userAttemped){
        log.info("User login api is requested, username: {}", userAttemped.getEmail());
        User u = userService.login(userAttemped);

        // If user exists, create a JWT and return it
        if(u != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("email", u.getEmail());
            String jwt = JwtUtils.createJWT(claims);
            Map<String, Object> map = new HashMap<>();
            map.put("jwt", jwt);
            map.put("user", u);
            return Result.success(map);
        }else {
            // If user does not exist, return error message
            return Result.error(CodeMessage.USER_NOT_EXIST);
        }

    }

    @PutMapping("/refreshtoken")
    public Result refreshToken(@RequestBody Map<String, String> map){

        try {
            String oldJwt = map.get("jwt");
            log.info("User refresh token api is requested, old jwt: {}", oldJwt);

            // if the jwt is null, reject the request
            if(StringUtils.isNullOrEmpty(oldJwt)){
                log.info("The request header jwt is null, return not logged in information");
                return Result.error(CodeMessage.USER_NOT_LOGIN);
            }

            // parse the jwt, if the jwt is invalid, return false
            Map<String, Object> oldClaims = JwtUtils.parseJWT(oldJwt);

            if(Long.parseLong(oldClaims.get("exp").toString()) * 1000 - new Date().getTime() > 1000 * 60 * 60 * 6 ){
                log.info("No need to refresh the token");
                return Result.error(CodeMessage.REFUSE_SERVICE);
            }else{
                Map<String, Object> newClaims = new HashMap<>();
                newClaims.put("id", oldClaims.get("id"));
                newClaims.put("email", oldClaims.get("email"));
                String jwt = JwtUtils.createJWT(newClaims);
                log.info("Jwt refreshed");
                return Result.success(jwt);
            }

        }catch (Exception e){
            e.printStackTrace();
            log.info("The request header jwt is invalid, return not logged in information");
            return Result.error(CodeMessage.USER_NOT_LOGIN);
        }
    }

}