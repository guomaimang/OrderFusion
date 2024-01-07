package tech.hirsun.orderfusion.controller;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/demo")
public class DemoController {
        @RequestMapping("/page")
        public String hello() {
            return "demo";
        }
}
