package com.zengkan.lankong.utils;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/02/25/20:46
 * @Description:
 **/
@Component
public class ShaUtil {
    /**
     * SHA加密类
     *
     * @param str 要加密的字符串
     * @return 加密后的字符串
     */
    public String getSha(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(str.getBytes());
            byte[] mdBytes = md.digest();
            StringBuilder hash = new StringBuilder();
            for (byte mdByte : mdBytes) {
                int temp;
                if (mdByte < 0) {
                    temp = 256 + mdByte;
                } else {
                    temp = mdByte;
                }
                if (temp < 16) {
                    hash.append("0");
                }
                hash.append(Integer.toString(temp, 16));
            }
            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
