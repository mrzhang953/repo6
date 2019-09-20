package com.yd.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author:易动学院 mrzhang
 * @date： 2019/9/18 20:28
 * 操作Redis 的一个工具类：
 *
 */
public class JedisUtil {
    private static   JedisPoolConfig poolConfig;
    private static   Properties prop;
    static{

        try {
            //使用类的加载器读取：
            InputStream inputStream = JedisUtil.class.getClassLoader().getResourceAsStream("redisConfig.properties");

            //准备一个Properties集合：
            prop= new Properties();

            //使用流加载集合：
            prop.load(inputStream);

            //获得一个连接池的配置对象：
            poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(Integer.parseInt(prop.getProperty("redis.maxTotal")));
            poolConfig.setMaxIdle(Integer.parseInt(prop.getProperty("redis.maxIdle")));
            poolConfig.setMinIdle(Integer.parseInt(prop.getProperty("redis.minIdle")));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 提供了一个工具方法： 方法能够对外提供一个jedis 对象：
     * @return
     */

    public static Jedis getJedis(){

        JedisPool pool = new JedisPool(poolConfig,prop.getProperty("redis.host"),Integer.parseInt(prop.getProperty("redis.port")));
        return  pool.getResource();

    }

    /**
     * 定义了一个释放资源的方法： 将jedis对象还会池子当中：
     * @param jedis
     */
    public static void close(Jedis jedis){

       jedis.close();

    }
}
