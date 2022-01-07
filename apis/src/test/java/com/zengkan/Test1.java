package com.zengkan;

import com.zengkan.lankong.pojo.User;
import com.zengkan.lankong.utils.MapUtils;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/11/27/17:38
 * @Description :
 * @modified By :
 **/

public class Test1 {
    @Test
    public void set() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        LocalDateTime now = LocalDateTime.now();
        now = now.minusDays(30);
        System.out.println(now.toString());
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.forEach(Integer ->{
            if (Integer.equals(1)) {
                return;
            }
            System.out.println(Integer.toString());
        });
    }
}
