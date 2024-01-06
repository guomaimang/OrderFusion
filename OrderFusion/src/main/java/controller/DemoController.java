/**
 * Author: Jiaming HAN (Hirsun Maxw) @PolyU-COMP
 * Homepage: hirsun.tech
 * License: MIT
 */

package controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "Hirsun");
        return "hello world";
    }

}
