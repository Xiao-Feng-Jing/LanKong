package com.zengkan.lankong.utils;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zengkan
 * @Date: 2021/03/12/19:48
 * @Description:
 **/
public class UUIDUtil {

    private UUIDUtil() {

    }
    public static String uuid(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-","");
    }
}
