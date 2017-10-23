package com.lhl;

import com.lhl.filter.LoginFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Liu Hailin
 * @create 2017-10-20 下午3:10
 **/
@SpringBootApplication
public class BootBizApp_B {

    public static void main(String[] args) {
        SpringApplication.run( BootBizApp_B.class, args );
    }

    @Bean
    public FilterRegistrationBean filterRegistration(LoginFilter filter) {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter( filter );
        filterRegistrationBean.addUrlPatterns( "/*" );
        filterRegistrationBean.addInitParameter( "sso-server-host", "sso.com:9000" );
        filterRegistrationBean.setName( "loginFilter" );
        return filterRegistrationBean;
    }

    @Bean
    public LoginFilter loginFilter() {
        return new LoginFilter();
    }
}
