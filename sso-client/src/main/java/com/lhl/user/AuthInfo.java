package com.lhl.user;

import lombok.Builder;
import lombok.Data;

/**
 * @author Liu Hailin
 * @create 2017-10-23 下午3:15
 **/
@Data
@Builder
public class AuthInfo {
    private User user;
    private String token;
    private boolean isPass = false;
}
