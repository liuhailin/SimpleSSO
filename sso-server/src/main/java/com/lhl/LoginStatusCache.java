package com.lhl;

import java.util.concurrent.ConcurrentHashMap;

import com.lhl.user.User;

/**
 * @author Liu Hailin
 * @create 2017-10-23 下午5:55
 **/
public class LoginStatusCache {

    private static ConcurrentHashMap<String,User> loginUser = new ConcurrentHashMap<>(100 );


    public static User getUserByToken(String token){
       return  loginUser.get( token );
    }


    public static void addUser(String token,User user){
        loginUser.putIfAbsent( token,user );

    }

    public static void removeUserByToken(String token){
        loginUser.remove( token );
    }
}
