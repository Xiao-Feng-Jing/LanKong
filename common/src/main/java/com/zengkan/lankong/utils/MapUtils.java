package com.zengkan.lankong.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author : ZengKan
 * @version :
 * @Date : 2021/09/15/21:25
 * @Description : Object与Map之间的转换
 * @modified By :
 **/
public class MapUtils{

    public static Map<String,Object> getObjectToMap(Object o) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class< ? > clazz = o.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(fieldName);
            if (value == null) {
                value = "";
            }
            map.put(fieldName, value);
        }
        return map;
    }

    public static Object getMapToObject(Map<String, Object> map,Class<?> beanClass) throws IllegalAccessException, InstantiationException {
        if (map == null) {
            return null;
        }
        Object object = beanClass.newInstance();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            if (map.containsKey(field.getName())) {
                field.set(object,map.get(field.getName()));
            }
        }
        return object;
    }
}
