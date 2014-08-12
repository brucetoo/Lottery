package com.bruce.Lottery.utils;

import com.bruce.Lottery.engine.UserEngine;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Bruce
 * Data 2014/8/12
 * Time 14:34.
 * 工厂类
 */
public class BeanFactory {

    //根据配置文件bean.properties 加载实例

    private static Properties properties;

    static {
        properties = new Properties();
        try {
            //bean.properties 必须杂src的根目录下
            //用类加载器 获取 文件流
            properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("bean.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //根据类名，反射获取实例
    public static UserEngine getImpl(Class clazz) {
        //getName 是包名和类名  getSimpleName 是类名
        String key = clazz.getSimpleName();

        String className = properties.getProperty(key);
        try {
            //反射获取 UserEngine实现类 实例
            return (UserEngine) Class.forName(className).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
