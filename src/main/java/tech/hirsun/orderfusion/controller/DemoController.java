package tech.hirsun.orderfusion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.hirsun.orderfusion.result.CodeMessage;
import tech.hirsun.orderfusion.result.Result;

@Controller
@RequestMapping("/demo")
public class DemoController {

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

    @RequestMapping("/db/get")
    public String db() {
            return "db";
    }



}
