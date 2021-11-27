package com.zengkan.lankong.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public static Map<String,Object> getObjectToMap(Object o) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Map<String, Object> map = new HashMap<>();
        Class< ? > clazz = o.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            String fieldName = field.getName();
            Object value = typeConversion(field, o);
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

    private static Object typeConversion(Field field, Object o) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Class<?> clazz = field.getType();
        Object val = null;
        Method m = null;
        if (!boolean.class.isAssignableFrom(clazz) && !Boolean.class.isAssignableFrom(clazz)) {
            /**
             * 这里需要说明一下：他是根据拼凑的字符来找你写的getter方法的
             * 在Boolean值的时候是isXXX（默认使用ide生成getter的都是isXXX）
             * 如果出现NoSuchMethod异常 就说明它找不到那个gettet方法 需要做个规范
             */
            m = (Method) o.getClass().getMethod(
                    "get" + getMethodName(field.getName()));
        }else {
            m = (Method) o.getClass().getMethod("is" +
                    getMethodName(field.getName()));
        }
        val = m.invoke(o);
        return val;
    }

    private static String getMethodName(String filename) {
        char[] item = filename.toCharArray();
        item[0] = (char) (item[0] - 'a' + 'A');
        return new String(item);
    }
}
