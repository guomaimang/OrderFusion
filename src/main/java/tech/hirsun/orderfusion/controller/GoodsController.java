package tech.hirsun.orderfusion.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.hirsun.orderfusion.result.ErrorMessage;
import tech.hirsun.orderfusion.result.Result;
import tech.hirsun.orderfusion.service.GoodsService;

@Slf4j
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "1") Integer pagenum,
                       @RequestParam(defaultValue = "10") Integer pagesize,
                       String keyword) {
        try {
            log.info("Request goods list, pageNum: {}, pageSize: {}, keyword: {}", pagenum, pagesize, keyword);
            return Result.success(goodsService.page(pagenum, pagesize, keyword));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error when admin request goods list");
            return Result.error(new ErrorMessage(50000, "Illegal Request"));
        }
    }

    @GetMapping("/info")
    public Result getGoodsInfo(@RequestParam Integer id) {
            try {
                log.info("Request goods info, id: {}", id);
                return Result.success(goodsService.getGoodsInfo(id));
            } catch (Exception e) {
                log.error("Error when admin request goods info");
                return Result.error(new ErrorMessage(50000, "Illegal Request"));
            }
        }
}