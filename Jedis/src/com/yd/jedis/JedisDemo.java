package com.yd.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author:易动学院 mrzhang
 * @date： 2019/9/18 18:57
 */
public class JedisDemo {

    /*
     向Redis 数据库当中存放一个值：username lisi


     */
    @Test
    public void test(){


        //获得一个jedis 对象：

        /*
           Jedis 使用的是无参数构造器：默认链接的是Localhost 6379
         */
        // Jedis jedis = new Jedis();

        Jedis jedis = new Jedis("192.168.0.117",6379);

        //存数据：
        //  jedis.set("username","lisi");

        String username = jedis.get("username");
        System.out.println(username);

        jedis.close();




    }

    //设置一个 age 22 并且设置该key的过期时间：10
    @Test
    public void test2(){



        Jedis jedis = new Jedis("192.168.0.117",6379);

       // jedis.setex("age",15,"22");

        String age = jedis.get("age");
        System.out.println(age);

        jedis.close();

    }

    //set
    @Test
    public void test3(){



        Jedis jedis = new Jedis("192.168.0.117",6379);

        //存值：
       jedis.sadd("set","lisi","wangwu","liuneng");

        Set<String> set = jedis.smembers("set");
        for (String name:set
             ) {
            System.out.println(name);
        }

        jedis.close();

    }

    //使用连接池：
    /*
      第一次和服务端建立连接的时候， 就初始化一些列的连接对象。 放在一个池子当中，使用连接对象的时候
      从池子当中获得一个链接对象。
       使用完毕后， 将连接对象换回池子当中：

     */

    //使用连接池： 获得链接对象
    @Test
    public void test4(){
        //创建一个连接池对象：
        JedisPool pool = new JedisPool("192.168.0.117",6379);

        //从池子当中获得一个链接对象：
        Jedis jedis = pool.getResource();

        //存值：
        jedis.set("username","wangwu");

        String username = jedis.get("username");
        System.out.println(username);

        //关闭资源：  还回池子当中
        jedis.close();


    }

    /**
     *使用连接池： 并且指定了连接池的一些默认参数：
     *
     */

    @Test
    public void test5(){

        //配置对象： JedisPoolConfig 对象是GenericObjectPoolConfig 对象的子类对象：
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(20);//最大闲置个数
        poolConfig.setMinIdle(10);//最小闲置个数
        poolConfig.setMaxTotal(50);//最大连接数


        //创建一个连接池对象：

        JedisPool pool = new JedisPool(poolConfig,"192.168.0.117",6379);


        Jedis resource = pool.getResource();
        resource.set("username","xx");

        String username = resource.get("username");
        System.out.println(username);

        resource.close();
      //  pool.close();


    }

//使用工具类测试：

    @Test
    public void test6(){

        Jedis resource = JedisUtil.getJedis();
        resource.set("username","uu");

        String username = resource.get("username");
        System.out.println(username);

        JedisUtil.close(resource);

    }

//存放list集合：
    @Test
    public void test7(){

        Jedis resource = JedisUtil.getJedis();
         resource.lpush("list","xxx","yyy");

        List<String> list = resource.lrange("list", 0, -1);
        for (String val:list
             ) {
            System.out.println(val);
        }



        JedisUtil.close(resource);

    }


    @Test
    public void test8(){

        //存值：
        Jedis resource = JedisUtil.getJedis();
         resource.hset("map","username","xiaojianren");
         resource.hset("map","add","panzhihua");

         //获得单个filed 对应的值：
     /*   String name = resource.hget("map", "username");
        System.out.println(name);*/

        Map<String, String> map = resource.hgetAll("map");
        for (String key :
            map.keySet() ) {
            String val = map.get(key);
            System.out.println(key+":"+val);
        }



        JedisUtil.close(resource);

    }


    //存放的值： sortedset
    @Test
    public void test9(){

        //存值：
        Jedis resource = JedisUtil.getJedis();

        resource.zadd("sortedset",10, "dj");
        resource.zadd("sortedset",50, "xj");

        //取值：
        Set<String> zrange = resource.zrange("sortedset", 0, -1);
       // resource.zrangeByScore("")
        for (String val:
             zrange) {
            System.out.println(val);
        }

        JedisUtil.close(resource);

    }


}
