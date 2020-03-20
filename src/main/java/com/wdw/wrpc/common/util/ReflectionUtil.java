package com.wdw.wrpc.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class ReflectionUtil {

    /**
     * 获取所有公共方法
     * @param clazz 类
     * @return 该类的所有公共方法
     */
    public static Method[] getPublicMethod(Class<?> clazz) {
        Method[] methods = clazz.getMethods();

        ArrayList<Method> retList = new ArrayList<>();
        for (Method m : methods) {
            if (Modifier.isPublic(m.getModifiers())) {
                retList.add(m);
            }
        }
        return retList.toArray(new Method[0]);
    }

    /**
     * 产生类的实例
     * @param clazz 类
     * @param <T> 类的类型
     * @return 该类的实例
     */
    public static <T> T newInstance(Class<T> clazz) {

        try {
            return (T) clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 反射调用方法
     * @param object 方法所属的实例
     * @param method 方法
     * @param args 方法的传入参数
     * @return 调用方法的结果
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object invoke(Object object, Method method, Object... args) {

        try {
            for(Object arg : args){
                System.out.println(arg.getClass().getName());
            }
            return method.invoke(object, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
