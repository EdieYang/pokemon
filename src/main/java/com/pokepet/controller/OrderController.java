package com.pokepet.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.OrderMall;
import com.pokepet.service.IOrderService;
import com.pokepet.util.wxpay.HttpRequest;
import com.pokepet.util.wxpay.OrderInfo;
import com.pokepet.util.wxpay.OrderReturnInfo;
import com.pokepet.util.wxpay.RandomStringGenerator;
import com.pokepet.util.wxpay.SignInfo;
import com.pokepet.util.wxpay.Signature;
import com.thoughtworks.xstream.XStream;

@ResponseResult
@RestController
@RequestMapping("/order")
public class OrderController {

	@Value("${linkPet.appId}")
	private String appId;

	@Value("${linkPet.merchantPayKey}")
	private String key;

	@Value("${linkPet.mchId}")
	private String mchId;

	@Value("${linkPet.orderNotifyUrl}")
	private String notifyUrl;
	// 交易类型
	private final String tradeType = "JSAPI";
	// 统一下单API接口链接
	private final String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 同一商品同一用户购买天数间隔
	 */
	private static final int DAY_RANGE_FOR_COMMODITY_TO_BUY = 7;

	/**
	 * 待支付订单有效期（分钟）
	 */
	private static final int EFFECTIVE_MINUTE_FOR_ORDER_TO_PAY = 30;

	@Autowired
	IOrderService orderService;

	@RequestMapping(value = "/checkConversion", method = RequestMethod.POST, consumes = "application/json")
	public JSONObject checkConversion(@RequestBody JSONObject data) {
		OrderMall order = JSONObject.toJavaObject(data, OrderMall.class);
		// 判断此人是否能建订单
		JSONObject check = orderService.checkBuyStatusByUserId(order.getUserId(), order.getCommodityId(),
				DAY_RANGE_FOR_COMMODITY_TO_BUY);
		return check;
	}

	@RequestMapping(value = "/crtOrder", method = RequestMethod.POST, consumes = "application/json")
	public JSONObject crtOrder(@RequestBody JSONObject data) {
		JSONObject result = new JSONObject();

		// Map<String, Object> map = new HashMap<String, Object>();
		double money = data.getDoubleValue("money");
		String title = data.getString("commodityName");
		String openId = data.getString("openId");

		try {
			OrderInfo wxPayOrder = new OrderInfo();
			wxPayOrder.setAppid(appId);
			wxPayOrder.setMch_id(mchId);
			wxPayOrder.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
			wxPayOrder.setBody(title);
			wxPayOrder.setOut_trade_no(RandomStringGenerator.getRandomStringByLength(32));
			wxPayOrder.setTotal_fee((int) (money * 100)); // 该金钱其实10 是 0.1元
			wxPayOrder.setSpbill_create_ip("127.0.0.1");
			wxPayOrder.setNotify_url(notifyUrl);
			wxPayOrder.setTrade_type(tradeType);
			wxPayOrder.setOpenid(openId);
			wxPayOrder.setSign_type("MD5");
			// 生成签名
			String sign = Signature.getSign(wxPayOrder, key);
			wxPayOrder.setSign(sign);

			System.out.println(JSONObject.toJSONString(wxPayOrder));
			String wxPayResult = HttpRequest.sendPost(url, wxPayOrder);
			System.out.println(wxPayResult);
			XStream xStream = new XStream();
			xStream.alias("xml", OrderReturnInfo.class);

			OrderReturnInfo returnInfo = (OrderReturnInfo) xStream.fromXML(wxPayResult);
			// 二次签名
			if ("SUCCESS".equals(returnInfo.getReturn_code())
					&& returnInfo.getReturn_code().equals(returnInfo.getResult_code())) {
				SignInfo signInfo = new SignInfo();
				signInfo.setAppId(appId);
				long time = System.currentTimeMillis() / 1000;
				signInfo.setTimeStamp(String.valueOf(time));
				signInfo.setNonceStr(RandomStringGenerator.getRandomStringByLength(32));
				signInfo.setRepay_id("prepay_id=" + returnInfo.getPrepay_id());
				signInfo.setSignType("MD5");
				// 生成签名
				String sign1 = Signature.getSign(signInfo, key);
				Map<String, Object> payInfo = new HashMap<String, Object>();
				payInfo.put("timeStamp", signInfo.getTimeStamp());
				payInfo.put("nonceStr", signInfo.getNonceStr());
				payInfo.put("package", signInfo.getRepay_id());
				payInfo.put("signType", signInfo.getSignType());
				payInfo.put("paySign", sign1);

				// 此处可以写唤起支付前的业务逻辑
				String orderId = UUID.randomUUID().toString();
				OrderMall order = JSONObject.toJavaObject(data, OrderMall.class);
				order.setOrderId(orderId);
				Calendar cal = Calendar.getInstance();
				order.setCreateTime(cal.getTime());// 订单创建时间
				cal.add(Calendar.MINUTE, EFFECTIVE_MINUTE_FOR_ORDER_TO_PAY);
				order.setEffectiveTime(cal.getTime());// 订单有效期
				orderService.createOrder(order);
				payInfo.put("orderId", orderId);

				// 业务逻辑结束
				result.put("status", 200);
				result.put("msg", "统一下单成功!");
				result.put("data", payInfo);

				return result;
			}
			result.put("status", 500);
			result.put("msg", "统一下单失败!");
			result.put("data", null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
