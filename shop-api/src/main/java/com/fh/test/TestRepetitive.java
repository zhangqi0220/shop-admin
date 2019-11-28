package com.fh.test;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TestRepetitive {
    //测试接口的幂等性
    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(200, 500, 2, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(200));
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //  阻塞线程
                        countDownLatch.await();
                  //  System.out.println("当前线程："+Thread.currentThread().getName());
                        sendHttp();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
        System.out.println("当前线程："+Thread.currentThread().getName());
        //-1  线程继续执行
        countDownLatch.countDown();

    }

    public static void sendHttp() {
        //打开浏览器
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        //输入网址
        HttpPost httpPost;
        httpPost = new HttpPost("http://localhost:8085/order/test.do");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("repetitive", "4f1c50c5-b686-4ae5-a1f9-b922f846bdbf");
        CloseableHttpResponse response = null;
        try {
            //回车
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String string = EntityUtils.toString(entity, "utf-8");
            System.out.println(string);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    @Test
    public void getToken() {
        //打开浏览器
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        //输入网址
        HttpPost httpPost;
        httpPost = new HttpPost("http://localhost:8085/user/getRedoInfo.do");
        httpPost.setHeader("Content-Type", "application/json");

        CloseableHttpResponse response = null;
        try {
            //回车
            response = httpClient.execute(httpPost);
            HttpEntity entity = (HttpEntity) response.getEntity();
            String string = EntityUtils.toString(entity, "utf-8");
            System.out.println(string);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
