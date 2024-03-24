package tech.hirsun.orderfusion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.hirsun.orderfusion.result.ErrorMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.SeckillEventService;

@Slf4j
@RestController
@RequestMapping("/seckill")

public class SeckillEventController {

    @Autowired
    private SeckillEventService seckillEventService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer pagenum,
                       @RequestParam(defaultValue = "10") Integer pagesize,
                       String keyword) {
        try {
            log.info("Request seckill list, pageNum: {}, pageSize: {}, keyword: {}", pagenum, pagesize, keyword);
            return Result.success(seckillEventService.page(pagenum, pagesize, keyword));
        } catch (Exception e) {
            log.error("Error when admin request seckill list");
            return Result.error(new ErrorMessage(50000, "Illegal Request"));
        }
    }

    @GetMapping("/info")
    public Result getSeckillInfo(@RequestParam Integer id) {
        try {
            log.info("Request seckill info, id: {}", id);
            return Result.success(seckillEventService.getSeckillInfo(id));
        } catch (Exception e) {
            log.error("Error when admin request seckill info");
            return Result.error(new ErrorMessage(50000, "Illegal Request"));
        }
    }

}
