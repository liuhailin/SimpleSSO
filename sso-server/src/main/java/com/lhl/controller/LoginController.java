package com.lhl.controller;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lhl.LoginStatusCache;
import com.lhl.user.AuthInfo;
import com.lhl.user.AuthInfo.AuthInfoBuilder;
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
import org.springframework.web.bind.annotation.ResponseBody;

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

    private static String USER_TOKEN = "user_token";

    @GetMapping("/auth")
    @ResponseBody
    public AuthInfo auth(@RequestParam(value = "authToken") String authToken) throws IOException {
        AuthInfoBuilder authInfoBuilder = AuthInfo.builder();

        authInfoBuilder.token( authToken );

        User user = LoginStatusCache.getUserByToken( authToken );

        if(null != user){
            authInfoBuilder.isPass( true );
            authInfoBuilder.user( user );
        }

        return authInfoBuilder.build();
    }

    @GetMapping("/login")
    public String login(ModelMap map, @RequestParam(value = "originUrl") String originUrl)
        throws IOException {

        map.put( "originUrl", originUrl );

        String authToken = CookieUtil.getCookieByName( request, "simple_sso" );

        if (StringUtils.isEmpty( authToken )) {
            return "login";
        }

        String cachedToken = (String)request.getSession().getAttribute( USER_TOKEN );
        if (!authToken.equals( cachedToken )) {
            return "login";
        }

        return "redirect:"+originUrl + "?token=" + authToken ;
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, ModelMap map, @RequestParam(value = "originUrl") String originUrl)
        throws IOException {

        log.info( "User [{}] is login success.", user.getUserName() );

        //TODO 默认所有用户登录成功
        String token = UUID.randomUUID().toString().replace( "-", "" );

        LoginStatusCache.addUser( token,user);

        Cookie tokenCookie = new Cookie( "simple_sso", token );

        tokenCookie.setDomain( "sso.com" );
        tokenCookie.setHttpOnly( true );
        tokenCookie.setPath( "/" );
        tokenCookie.setMaxAge( 10 * 60 );

        request.getSession().setAttribute( USER_TOKEN, token );

        response.addCookie( tokenCookie );

        return "redirect:"+ originUrl + "?token=" + token ;
    }
}
