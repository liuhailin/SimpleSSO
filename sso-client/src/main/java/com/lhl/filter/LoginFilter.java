package com.lhl.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lhl.user.User;
import org.springframework.util.StringUtils;

/**
 * @author Liu Hailin
 * @create 2017-10-20 下午4:19
 **/
public class LoginFilter implements Filter {

    private final String USER_SESSION_KEY = "user_ses_key";

    private String ssoServerHost;

    private String ssoProtocol;

    private String ssoContextPath = "/sso/auth";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ssoServerHost = filterConfig.getInitParameter( "sso-server-host" );
        ssoProtocol = filterConfig.getInitParameter( "sso-protocol" );
        if(StringUtils.isEmpty( ssoProtocol )){
            ssoProtocol = "http";
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest)servletRequest;
            HttpServletResponse response = (HttpServletResponse)servletResponse;
            String authToken = request.getParameter( "token" );
            if(StringUtils.isEmpty( authToken )){
                User user = (User)request.getSession().getAttribute( "user_ses_key" );
                if (null == user) {
                    redirctSSO( request, response );
                    return;
                }
            }
            //TODO 去sso验证toke合法
            boolean isSafe = requestSSOValidateToken(authToken);
            if(!isSafe){
                redirctSSO( request, response );
                return;
            }
        }

        filterChain.doFilter( servletRequest, servletResponse );
    }

    private void redirctSSO(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String originUrl = request.getRequestURL().toString();
        response.sendRedirect(
            ssoProtocol + "://" + ssoServerHost  + ssoContextPath + "?originUrl=" + originUrl );
    }

    private boolean requestSSOValidateToken(String authToken) {
        return true;
    }

    @Override
    public void destroy() {

    }
}
