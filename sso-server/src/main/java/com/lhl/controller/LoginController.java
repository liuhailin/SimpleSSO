package com.lhl.controller;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lhl.user.User;
import com.lhl.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Liu Hailin
 * @create 2017-10-20 下午3:30
 **/
@Controller
@Slf4j
@RequestMapping("/sso")
public class LoginController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @GetMapping("/auth")
    public String auth(@RequestParam(value = "originUrl",required = false) String originUrl) throws IOException {

        String authToken = CookieUtil.getCookieByName( request,"simple_sso" );
        if(StringUtils.isEmpty( authToken )){
            if(!StringUtils.isEmpty( originUrl )){
                return "redirect:/sso/login?originUrl="+originUrl;
            }
            return "redirect:/sso/login";
        }

        response.sendRedirect( originUrl+"?token="+authToken );

        return null;
    }



    @GetMapping("/login")
    public String login(ModelMap map,@RequestParam(value = "originUrl",required = false) String originUrl) {
        map.put( "originUrl",originUrl );
        return "login";
    }


    @PostMapping("/login")
    public void login(@ModelAttribute User user, ModelMap map,@RequestParam(value = "originUrl") String originUrl)
        throws IOException {

        log.info( "User [{}] is login success.", user.getUserName() );

        String token = UUID.randomUUID().toString().replace("-", "");

        Cookie tokenCookie = new Cookie("simple_sso", token);

        tokenCookie.setDomain( "sso.com" );
        tokenCookie.setHttpOnly( true );
        tokenCookie.setPath( "/" );
        tokenCookie.setMaxAge(10*60);

        response.addCookie( tokenCookie );

        response.sendRedirect( originUrl+"?token="+token );
    }
}
