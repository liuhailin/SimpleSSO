package com.lhl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Liu Hailin
 * @create 2017-10-20 下午7:17
 **/
@Controller
public class Home {

    @GetMapping("/home")
    public String home(){
        return "homepage";
    }

}
