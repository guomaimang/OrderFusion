package tech.hirsun.orderfusion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class DemoController {
    @RequestMapping("/page")
    public String page() {
        return "demo";
    }

    @ResponseBody
    @RequestMapping("/text")
    public String text() {
        return "text";
    }

    

    @RequestMapping("/db/get")
    public String db() {
            return "db";
    }




}
