/*package com.pokepet.controller;


import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pokept.test.Configure;
import com.pokept.test.HttpRequest;
import com.pokept.test.OrderInfo;
import com.pokept.test.OrderReturnInfo;
import com.pokept.test.PayUtil;
import com.pokept.test.RandomStringGenerator;
import com.pokept.test.SignInfo;
import com.pokept.test.Signature;
import com.thoughtworks.xstream.XStream;

*//**
* @Author: YFei
* @Date: Created in 18:10 2018/7/25
* @Description:
*//*
@RestController
@RequestMapping(value = "/api/v1")
public class WXAppletPayCtrl {
  
   private static final long serialVersionUID = 1L;

   private String notify_url = "http://da17259640.51mypc.cn:10554/pokemon/api/v1/weixin/callback";
   //交易类型
   private final String trade_type = "JSAPI";
   //统一下单API接口链接
   private final String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

   *//**
    *
    * @param request
    * @return
    * @throws UnsupportedEncodingException
    * @throws DocumentException
    *//*
   @RequestMapping(
           value = "/weixin/payment",
           method = RequestMethod.POST
   )
   public Map payment(HttpServletRequest httpServletRequest) {
       Map map = new HashMap();
       String money = "10";
       String title = "商品名字";
       String openId = "o-f565QHtzrrebxS_vQwASOaahX8";
      
       try {
           OrderInfo order = new OrderInfo();
           order.setAppid(Configure.getAppID());
           order.setMch_id(Configure.getMch_id());
           order.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
           order.setBody(title);
           order.setOut_trade_no(RandomStringGenerator.getRandomStringByLength(32));
           order.setTotal_fee(Integer.parseInt(money));     // 该金钱其实10 是 0.1元
           order.setSpbill_create_ip("127.0.0.1");
           order.setNotify_url(notify_url);
           order.setTrade_type(trade_type);
           order.setOpenid(openId);
           order.setSign_type("MD5");
           //生成签名
           String sign = Signature.getSign(order);
           order.setSign(sign);

           System.out.println(JSONObject.toJSONString(order));
           String result = HttpRequest.sendPost(url, order);
           System.out.println(result);
           XStream xStream = new XStream();
           xStream.alias("xml", OrderReturnInfo.class);

           OrderReturnInfo returnInfo = (OrderReturnInfo)xStream.fromXML(result);
           // 二次签名
           if ("SUCCESS".equals(returnInfo.getReturn_code()) && returnInfo.getReturn_code().equals(returnInfo.getResult_code())) {
               SignInfo signInfo = new SignInfo();
               signInfo.setAppId(Configure.getAppID());
               long time = System.currentTimeMillis()/1000;
               signInfo.setTimeStamp(String.valueOf(time));
               signInfo.setNonceStr(RandomStringGenerator.getRandomStringByLength(32));
               signInfo.setRepay_id("prepay_id="+returnInfo.getPrepay_id());
               signInfo.setSignType("MD5");
               //生成签名
               String sign1 = Signature.getSign(signInfo);
               Map payInfo = new HashMap();
               payInfo.put("timeStamp", signInfo.getTimeStamp());
               payInfo.put("nonceStr", signInfo.getNonceStr());
               payInfo.put("package", signInfo.getRepay_id());
               payInfo.put("signType", signInfo.getSignType());
               payInfo.put("paySign", sign1);
               map.put("status", 200);
               map.put("msg", "统一下单成功!");
               map.put("data", payInfo);

               // 此处可以写唤起支付前的业务逻辑
               
               // 业务逻辑结束
               return map;
           }
           map.put("status", 500);
           map.put("msg", "统一下单失败!");
           map.put("data", null);
           return map;
       } catch (Exception e) {
           e.printStackTrace();
       }
       return null;
   }

   *//**
    * 微信小程序支付成功回调函数
    * @param request
    * @param response
    * @throws Exception
    *//*
   @RequestMapping(value = "/weixin/callback")
   public void wxNotify(HttpServletRequest request,HttpServletResponse response) throws Exception{
       BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream)request.getInputStream()));
       String line = null;
       StringBuilder sb = new StringBuilder();
       while((line = br.readLine()) != null){
           sb.append(line);
       }
       br.close();
       //sb为微信返回的xml
       String notityXml = sb.toString();
       String resXml = "";
       System.out.println("接收到的报文：" + notityXml);

       Map map = PayUtil.doXMLParse(notityXml);

       String returnCode = (String) map.get("return_code");
       if("SUCCESS".equals(returnCode)){
           //验证签名是否正确
           Map<String, String> validParams = PayUtil.paraFilter(map);  //回调验签时需要去除sign和空值参数
           String validStr = PayUtil.createLinkString(validParams);//把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
           String sign = PayUtil.sign(validStr, Configure.getKey(), "utf-8").toUpperCase();//拼装生成服务器端验证的签名
           // 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了
          
           //根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
           if(sign.equals(map.get("sign"))){
               *//**此处添加自己的业务逻辑代码start**//*
                 // bla bla bla....
               *//**此处添加自己的业务逻辑代码end**//*
               //通知微信服务器已经支付成功
               resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                       + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
           } else {
               System.out.println("微信支付回调失败!签名不一致");
           }
       }else{
           resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                   + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
       }
       System.out.println(resXml);
       System.out.println("微信支付回调数据结束");

       BufferedOutputStream out = new BufferedOutputStream(
               response.getOutputStream());
       out.write(resXml.getBytes());
       out.flush();
       out.close();
   }

}
*/