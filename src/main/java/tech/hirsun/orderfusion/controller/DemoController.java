package tech.hirsun.orderfusion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo")
public class DemoController {
        @RequestMapping("/page")
        public String page() {
            return "demo";
        }
}
