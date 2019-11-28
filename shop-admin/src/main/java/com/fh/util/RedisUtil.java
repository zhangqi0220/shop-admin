package com.fh.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class RedisUtil{
  
   //Redis服务器IP
   private static String URL = "192.168.216.136";
   //Redis的端口号
   private static int PORT = 6379;
   //可用连接实例的最大数目，默认值为8
   private static int MAX_ACTIVE = 1024;
   //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8
   private static int MAX_IDLE = 200;
   //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时
   //如果超过等待时间，则直接抛出JedisConnectionException
   private static int MAX_WAIT = 20000;
   private static int TIMEOUT = 10000;
   //在borrow一个jedis实例时，是否提前进行validate操作,如果为true，则得到的jedis实例均是可用的
   private static boolean TEST_ON_BORROW = true;
   
   private static JedisPool jedisPool = null;

   /**
    * 初始化Redis连接池
    */
   static {
      try{
          //1.设置连接池的配置对象
         JedisPoolConfig config = new JedisPoolConfig();
         //设置池中最大连接数
         config.setMaxTotal(MAX_ACTIVE);
          //设置空闲时池中保有的最大连接数
         config.setMaxIdle(MAX_IDLE);

         config.setMaxWaitMillis(MAX_WAIT);
         config.setTestOnBorrow(TEST_ON_BORROW);
          //2.设置连接池对象
         jedisPool = new JedisPool(config, URL, PORT, TIMEOUT);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * 获取Jedis实例
    * @return
    */
   public synchronized static Jedis getJedis() {
        Jedis redis = null;
        try{
           if(jedisPool != null){
              redis = jedisPool.getResource();
              return redis;
           }else{
              return null;
           }
        } catch (Exception e){
             e.printStackTrace();
             return null;
        }
   }

   /**
    * 释放jedis资源
    * @param jedis
    */
   public static void returnResource(final Jedis jedis){
       if(jedis != null){
           jedisPool.returnResource(jedis);
       }
   }

   /*
	* Test
	*/

    public static void main(String[] args) {
        //测试Redis服务器的连接
        Jedis jedis = new Jedis("192.168.216.132",6381);
        System.out.println(jedis.ping());
        //测试Redis取数(需先确定redis key存储类型,这里是hashMap)
        /* Jedis jedis = getJedis();
         jedis.set("jb", "222");
        String jb = jedis.get("jb");
        System.out.println(jb);*/
    }

}