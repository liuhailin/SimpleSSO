package com.lhl.controller;

import javax.servlet.http.HttpServletRequest;

import com.lhl.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Liu Hailin
 * @create 2017-10-20 下午7:17
 **/
@Controller
public class Home {

    @GetMapping("/home")
    public String home(HttpServletRequest request, ModelMap map){
        User user = (User)request.getSession().getAttribute( "user_ses_key" );

        map.put( "username",user.getUserName() );
        return "homepage";
    }

}
