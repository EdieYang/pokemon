package com.pokepet.utils;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.*;
import java.util.UUID;
import java.nio.charset.Charset;

/**
 * Created by Fade on 2018/8/27.
 */
public class HttpPostUtils {



    public static String httpPostWithJson(String param, String url){
        boolean isSuccess = false;

        HttpPost post = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();

            // 设置超时时间
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);

            post = new HttpPost(url);
            // 构造消息头
            post.setHeader("Content-type", "text/xml;charset=utf-8");
            post.setHeader("Connection", "Close");
            String sessionId = getSessionId();
            post.setHeader("SessionId", sessionId);

            // 构建消息实体
            StringEntity entity = new StringEntity(param, Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
            // 发送Json格式的数据请求
            entity.setContentType("text/xml");
            post.setEntity(new StringEntity(param,"UTF-8"));

            HttpResponse response = httpClient.execute(post);

            // 检验返回码
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode != HttpStatus.SC_OK){
                isSuccess = false;

            }else{
                int retCode = 0;
                String sessendId = "";
                // 返回码中包含retCode及会话Id
                for(Header header : response.getAllHeaders()){
                    if(header.getName().equals("retcode")){
                        retCode = Integer.parseInt(header.getValue());
                    }
                    if(header.getName().equals("SessionId")){
                        sessendId = header.getValue();
                    }
                }

                HttpEntity httpEntity=response.getEntity();
                if (httpEntity != null){
                    InputStream instreams = httpEntity.getContent();
                    String str = convertStreamToString(instreams);
                    System.out.println("Response:" + "\n" + str);

                    return str;
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
        }finally{
            if(post != null){
                try {
                    post.releaseConnection();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();



        String line = null;

        try {

            while ((line = reader.readLine()) != null) {

                sb.append(line + "/n");

            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                is.close();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }



        return sb.toString();

    }




    public static String inputStream2String(InputStream is)throws IOException {
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        int   i=-1;
        while((i=is.read())!=-1){
            baos.write(i);
        }
        return   baos.toString();
    }






    // 构建唯一会话Id
    public static String getSessionId(){
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
    }
}
