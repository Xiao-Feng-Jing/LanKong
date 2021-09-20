package com.zengkan.lankong.shiro.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/08/31/21:27
 * @Description:
 **/
public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = -4513474369798288341L;
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
