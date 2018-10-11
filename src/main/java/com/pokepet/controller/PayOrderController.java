/*package com.pokepet.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.OrderPay;
import com.pokepet.model.PetWeapon;
import com.pokepet.model.User;
import com.pokepet.service.IOrderService;
import com.pokepet.service.IPetWeaponService;
import com.pokepet.service.IUserService;
import com.pokepet.util.CommonUtil;
import com.pokepet.util.HttpUtil;
import com.pokepet.util.wxpay.WXPayUtil;

*//**
 * Created by Fade on 2018/8/20.
 *//*
@ResponseResult
@RestController
@RequestMapping("/wxPay")
public class PayOrderController {

    private static final String APPID="wx21a19c3555f7da35";

    private static final String MCHID="1511048701";

    private static final String KEY="31011219900601461x31011319900601";

    private static final String UNIFIEDORDER_URL="https://api.mch.weixin.qq.com/pay/unifiedorder";

    private static final Logger logger= LoggerFactory.getLogger(PayOrderController.class);


    @Autowired
    IUserService userService;

    @Autowired
    IPetWeaponService petWeaponService;

    @Autowired
    IOrderService orderService;


    @RequestMapping(value = "/createOrderService",method = RequestMethod.POST)
    public JSONObject createOrderService(HttpServletRequest request){
        JSONObject orderReturn=new JSONObject();

        String userId=request.getParameter("userId");
        String productId=request.getParameter("productId");
        String productType=request.getParameter("productType");

        if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(productId)){
            orderReturn.put("success",false);
            orderReturn.put("msg","请求参数缺失");
            return orderReturn;
        }

        //判断用户是否存在
        User user= userService.getUserInfo(userId);
        if (user==null) {
            orderReturn.put("success",false);
            orderReturn.put("msg","请求用户不存在");
            return orderReturn;
        }

        String openId=user.getOpenId();
        if (StringUtils.isEmpty(openId)) {
            orderReturn.put("success",false);
            orderReturn.put("msg","请求用户openId不存在");
            return orderReturn;
        }


        String totalMoney="";
        int buyMoney=0;
        String body="";

        switch (productType){
            case "weapon":
                PetWeapon petWeapon=petWeaponService.getWeaponByWeaponId(productId);
                if(petWeapon==null){
                    orderReturn.put("success",false);
                    orderReturn.put("msg","请求购买商品不存在");
                    return orderReturn;
                }

                buyMoney=petWeapon.getBuyMoney()*100;

                //判断商品可买状态
                if(buyMoney==-1){
                    orderReturn.put("success",false);
                    orderReturn.put("msg","请求购买商品暂时无法购买");
                    return orderReturn;
                }

                totalMoney= String.valueOf(buyMoney);
                body="test";
                break;
            // TODO: 2018/8/21  supply total money


        }

        //创建hashmap(用户获得签名)
        Map<String, String> paraMap = new HashMap<String, String>();

        //设置随机字符串
        String nonceStr = UUID.randomUUID().toString().replaceAll("-", "");

        //设置商户订单号
        String outTradeNo =  UUID.randomUUID().toString().replaceAll("-", "");



        //设置请求参数(小程序ID)
        paraMap.put("appid", APPID);
        //设置请求参数(商品描述)
        paraMap.put("body",body);
        //设置请求参数(商户号)
        paraMap.put("mch_id", MCHID);
        //设置请求参数(随机字符串)
        paraMap.put("nonce_str", nonceStr);
        //设置请求参数(通知地址)
        //request.getScheme() +"://" + request.getServerName()  + ":" +request.getServerPort() +request.getContextPath()
        paraMap.put("notify_url","http://airpnp.s1.natapp.cc/pokemon/wxPay/payCallback");
        //设置请求参数(openid)(在接口文档中 该参数 是否必填项 但是一定要注意 如果交易类型设置成'JSAPI'则必须传入openid)
        paraMap.put("openid", openId);
        //设置请求参数(商户订单号)
        paraMap.put("out_trade_no", "20150806125346");
        //设置请求参数(终端IP)
        paraMap.put("spbill_create_ip", "124.77.178.208");
        //设置请求参数(总金额)
        paraMap.put("total_fee", totalMoney);
        //设置请求参数(交易类型)
        paraMap.put("trade_type", "JSAPI");
        String xmlParam="";
        try {
            xmlParam=WXPayUtil.generateSignedXml(paraMap,KEY);
            logger.info(xmlParam);
            logger.info("判断签名正确"+ WXPayUtil.isSignatureValid(xmlParam,KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //发送请求(POST)(获得数据包ID)(这有个注意的地方 如果不转码成ISO8859-1则会告诉你body不是UTF8编码 就算你改成UTF8编码也一样不好使 所以修改成ISO8859-1)
            System.out.println(HttpUtil.doPost(UNIFIEDORDER_URL,xmlParam));
            Map<String,String> map = CommonUtil.doXMLParse(CommonUtil.httpsRequest(UNIFIEDORDER_URL,"POST",xmlParam));
            //应该创建 支付表数据
            if(map!=null){
                OrderPay orderPay=new OrderPay();
                orderPay.setOrderId(outTradeNo);
                orderPay.setUserId(userId);
                orderPay.setCommodityId(productId);

                switch (productType){
                    case "product":
                        orderPay.setCommodityType("0");
                        orderPay.setOrderType("0");
                        break;
                    case "weapon":
                        orderPay.setCommodityType("1");
                        orderPay.setOrderType("1");
                        break;
                    case "supply":
                        orderPay.setCommodityType("2");
                        orderPay.setOrderType("1");
                        break;

                }

                orderPay.setOrderStatus("0");
                orderPay.setPrice(buyMoney);
                orderPay.setCreateTime(new Date());

                orderService.createOrderPay(orderPay);


                orderReturn.put("prepayId", map.get("prepay_id"));
                orderReturn.put("outTradeNo", paraMap.get("out_trade_no"));
                return orderReturn;

            }
            //将 数据包ID 返回

            logger.info("[微信下单接口返回结果]"+map);
        } catch (UnsupportedEncodingException e) {
            logger.info("微信 统一下单 异常："+e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            logger.info("微信 统一下单 异常："+e.getMessage());
            e.printStackTrace();
        }
        logger.info("微信 统一下单 失败");
        return orderReturn;






    }


    @RequestMapping("/payCallback")
    public String payCallBack(){
        return "执行回调函数";
    }

    
    *//**
	 * 订单列表（分页）
	 * 
	 * @param search
	 * @param typeList
	 * @param brandList
	 * @param pageNum
	 * @param pageSize
	 * @return
	 *//*
	@RequestMapping(value = "/orderList", method = RequestMethod.GET)
	public JSONObject geOrderList(@RequestParam("search") String search,
			@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("search", search);
		return orderService.getOrderList(param, pageNum, pageSize);
	}
	
	
	*//**
	 * 根据orderId获取订单详情
	 * @param orderId
	 * @return
	 *//*
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public JSONObject geOrderInfo(@RequestParam("orderId") String orderId) {
		JSONObject result = new JSONObject();
		result.put("orderInfo", orderService.getOrder(orderId));
		return result;
	}
	

}
*/