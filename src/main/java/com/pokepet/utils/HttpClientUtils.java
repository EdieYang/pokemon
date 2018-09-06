package com.pokepet.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Fade on 2018/6/20.
 */
public class HttpClientUtils {


    public static String httpGet(String url){
        CloseableHttpClient httpCilent = HttpClients.createDefault();//Creates CloseableHttpClient instance with default configuration.
        HttpGet httpGet = new HttpGet(url);
        String srtResult="";
        try {
            CloseableHttpResponse response=httpCilent.execute(httpGet);
            srtResult = EntityUtils.toString(response.getEntity());//获得返回的结果
            return srtResult;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpCilent.close();//释放资源
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }






}
