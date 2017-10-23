package com.lhl.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Liu Hailin
 * @create 2017-10-20 下午5:34
 **/
public class CookieUtil {

    public static String getCookieByName(HttpServletRequest request, String name) {

        Cookie[] cookies = request.getCookies();

        if (null != cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals( name )) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

}
