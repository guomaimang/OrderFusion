package tech.hirsun.orderfusion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.orderfusion.pojo.User;
import tech.hirsun.orderfusion.result.CodeMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.UserService;


@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/list")
    public Result page(@RequestParam(defaultValue = "1") Integer pagenum,
                       @RequestParam(defaultValue = "10") Integer pagesize,
                       String keyword){
        log.info("Admin request user list, pageNum: {}, pageSize: {}, keyword: {}", pagenum, pagesize, keyword);

        try {
            return Result.success(userService.page(pagenum, pagesize, keyword));
        }catch (Exception e){
            log.error("Error when admin request user list");
            return Result.error(new CodeMessage(50000, "Illegal Request"));
        }
    }

    @PostMapping("/user/add")
    public Result add(@RequestBody User user){
        log.info("Admin request add user, name: {}, email: {}", user.getName(), user.getEmail());
        try {
            userService.add(new User(user.getName(), user.getEmail(), user.getPassword()));
            return Result.success("Success");
        }catch (Exception e){
            log.error("Error when add user");
            return Result.error(new CodeMessage(50000, "Illegal Request"));
        }
    }

    @PutMapping("/user/lockswitch")
    public Result lockSwitch(@RequestBody User user){
        log.info("Admin request lock switch, id: {}", user.getId());
        try {
            userService.lockSwitch(user);
            return Result.success("Success");
        }catch (Exception e){
            log.error("Error when lock switch");
            return Result.error(new CodeMessage(50000, "Illegal Request"));
        }
    }

    @PutMapping("/user/update")
        public Result edit(@RequestBody User user){
            log.info("Admin request edit user, id: {}", user.getId());
            try {
                userService.update(user);
                return Result.success("Success");
            }catch (Exception e){
                log.error("Error when edit user");
                return Result.error(new CodeMessage(50000, "Illegal Request"));
            }
        }
}
