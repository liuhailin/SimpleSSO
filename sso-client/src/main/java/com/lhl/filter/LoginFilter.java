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

import com.lhl.user.AuthInfo;
import com.lhl.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * @author Liu Hailin
 * @create 2017-10-20 下午4:19
 **/
public class LoginFilter implements Filter {

    private final String USER_SESSION_KEY = "user_ses_key";

    private String ssoServerHost;

    private String ssoProtocol;

    private String ssoLoginContextPath = "/sso/login";
    private String ssoAuthContextPath = "/sso/auth";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ssoServerHost = filterConfig.getInitParameter( "sso-server-host" );
        ssoProtocol = filterConfig.getInitParameter( "sso-protocol" );
        if (StringUtils.isEmpty( ssoProtocol )) {
            ssoProtocol = "http";
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest)servletRequest;
            HttpServletResponse response = (HttpServletResponse)servletResponse;

            User user = (User)request.getSession().getAttribute( USER_SESSION_KEY );
            if (null != user) {
                filterChain.doFilter( servletRequest, servletResponse );
                return;
            }
            String authToken = request.getParameter( "token" );
            if (StringUtils.isEmpty( authToken )) {
                redirctSSO( request, response );
                return;
            }
            AuthInfo authInfo = requestSSOValidateToken( authToken );
            if (null == authInfo || !authInfo.isPass()) {
                redirctSSO( request, response );
                return;
            }
            request.getSession().setAttribute( USER_SESSION_KEY, authInfo.getUser() );
        }

        filterChain.doFilter( servletRequest, servletResponse );
    }

    private void redirctSSO(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String originUrl = request.getRequestURL().toString();
        response.sendRedirect(
            ssoProtocol + "://" + ssoServerHost + ssoLoginContextPath + "?originUrl=" + originUrl );
    }

    private AuthInfo requestSSOValidateToken(String authToken) {
        ResponseEntity<AuthInfo> infoResponseEntity = restTemplate.getForEntity(
            ssoProtocol + "://" + ssoServerHost + ssoAuthContextPath + "?authToken={authToken}", AuthInfo.class,
            authToken );
        return infoResponseEntity.getBody();
    }

    @Override
    public void destroy() {

    }
}
