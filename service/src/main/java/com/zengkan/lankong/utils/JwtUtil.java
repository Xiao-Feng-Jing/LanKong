package com.zengkan.lankong.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/01/25/15:05
 * @Description:
 **/
@Slf4j
public class JwtUtil {
    private JwtUtil() {

    }
    /**
     * @Description:
     * */
    public static String createToken(Map<String, Object> userMap){
        JwtBuilder jwtBuilder = Jwts.builder();
        return jwtBuilder
                //用户信息
                .setClaims(userMap)
                //设置token主题
                .setSubject("login")
                //设置颁发时间
                .setIssuedAt(new Date())
                //设置tokenid
                .setId(UUID.randomUUID().toString())
                //颁发签名
                .signWith(SignatureAlgorithm.HS256,getSecret())
                //生成token
                .compact();
    }

    /**
     * token是否过期
     * @return false：过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(token).getBody();
            return true;
        }catch (ExpiredJwtException e){
            log.error("token 过期或者不存在");
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     *@Description:  jwt解析器
     * */
    public static Claims checkToken(String token){
        //获取解析器，传入解码秘钥
        //System.out.println(clamit);
        Claims claims = null;

        try {
            claims = Jwts.parser().setSigningKey(getSecret())
                    .parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            log.error("token 不存在");
            return claims;
        }
    }

    private static SecretKey getSecret() {
        String key = "sdafasdfsdfs";
        byte[] encodedKey = Base64.getDecoder().decode(key);
        return new SecretKeySpec(encodedKey,0, encodedKey.length,"AES");
    }
}
