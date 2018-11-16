package com.pokepet.util.wxConfig;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pokepet.util.HttpUtil;

public class Token {

    private static String access_token="";
    
    private static String jsapi_ticket = "";
    
    public static int time = 0;
    
    private static int expires_in = 7200;
    
    static{
        Thread t = new Thread(new Runnable(){  
            public void run(){  
                do{
                    time++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }while(true);
            }});  
        t.start();
    }
    
    public static String getToken(){
        if("".equals(access_token)||access_token==null){
            send();            
        }else if(time>expires_in){
            //当前token已经失效，从新获取信息
            send();
        }
        return access_token;
    }
    public static String getTicket(){
        if("".equals(jsapi_ticket)||jsapi_ticket==null){
            jsapi_ticket=send();
        }else if(time>expires_in){
            //当前token已经失效，从新获取信息
            jsapi_ticket=send();
        }
        return jsapi_ticket;
    }
    private static String send(){
        String url = WXConfig.server_token_url+"&appid="+WXConfig.appid+"&secret="+WXConfig.appsecret;
        String res = HttpUtil.doGet(url);
        JSONObject resJ= JSON.parseObject(res);
        access_token = resJ.getString("access_token");
        String ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+access_token+"&type=jsapi";
        String resTicket= HttpUtil.doGet(ticket_url);
        JSONObject resTicketJ=JSON.parseObject(resTicket);
        time = 0;
        return resTicketJ.getString("ticket");
    }
}