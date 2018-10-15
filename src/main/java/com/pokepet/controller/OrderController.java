package com.pokepet.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import com.pokepet.model.User;
import com.pokepet.service.IPetWeaponService;
import com.pokepet.service.IUserService;
import com.pokepet.util.wxpay.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.pokepet.annotation.ResponseResult;
import com.pokepet.model.OrderMall;
import com.pokepet.service.IOrderService;
import com.thoughtworks.xstream.XStream;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	/**
	 * 取消订单时效(秒)
	 */
	private static final int ORDER_CANCEL_DURATION=30*60;

	/**
	 * 订单未支付状态
	 */
	private static final String ORDER_UNPAYED_STATUS="0";

	/**
	 * 订单已支付状态
	 */
	private static final String ORDER_PAYED_STATUS="1";

	/**
	 * 订单确认状态
	 */
	private static final String ORDER_CONFIRM_STATUS="3";

	/**
	 * 订单取消状态
	 */
	private static final String ORDER_CANCEL_STATUS="4";

	/**
	 * 购买方式:现金
	 */
	private static final String BUY_TYPE_CASH="0";

	/**
	 * 购买方式:虚拟币
	 */
	private static final String BUY_TYPE_COIN="1";


	@Autowired
	IOrderService orderService;

	@Autowired
	IUserService userService;

	@Autowired
	IPetWeaponService petWeaponService;


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

		//判断订单类型
		String orderType=data.getString("orderType");
		if(orderType.equals("1")){ //虚拟订单,用于购买虚拟商品
			String userId=data.getString("userId");
			String weaponId=data.getString("commodityId");
			//调用购买接口
			JSONObject returnResult=petWeaponService.buyWeapon(userId,weaponId,"chip",null);

			if(returnResult.getBooleanValue("flag")){

				//创建订单
				OrderMall order = JSONObject.toJavaObject(data, OrderMall.class);
				order.setOrderId(RandomStringGenerator.getRandomStringByLength(32));
				order.setOrderStatus(ORDER_CONFIRM_STATUS);
				order.setBuyType(BUY_TYPE_COIN);
				order.setMoney(0);
				order.setOrderType("1");//虚拟订单
				order.setPayType("0");//线上付款
				order.setCreateTime(new Date());
				order.setPayTime(new Date());
				orderService.createOrder(order);
				result.put("status", 200);
				result.put("msg", "兑换成功!");
				result.put("data", "");
				return result;
			}else{
				result.put("status", 500);
				result.put("msg", returnResult.getString("msg"));
				result.put("data", "");
				return result;
			}

		}



		if(orderType.equals("0")) { //虚拟订单,用于换购真实商品
			double money = data.getDoubleValue("money");
			data.put("money", (int) (money * 100)); //将金额转为分
			String title = data.getString("commodityName");
			String openId = data.getString("openId");

			try {
				String orderId = RandomStringGenerator.getRandomStringByLength(32);
				OrderInfo wxPayOrder = new OrderInfo();
				wxPayOrder.setAppid(appId);
				wxPayOrder.setMch_id(mchId);
				wxPayOrder.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
				wxPayOrder.setBody(title);
				wxPayOrder.setOut_trade_no(orderId);
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
					OrderMall order = JSONObject.toJavaObject(data, OrderMall.class);
					order.setOrderId(orderId);
					Calendar cal = Calendar.getInstance();
					order.setCreateTime(cal.getTime());// 订单创建时间
					cal.add(Calendar.MINUTE, EFFECTIVE_MINUTE_FOR_ORDER_TO_PAY);
					order.setEffectiveTime(cal.getTime());// 订单有效期
					order.setBuyType("3");//购买方式为换购(现金+金币)
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

		}

		return result;

	}

	/**
	 * 对未支付订单进行支付操作
	 * 说明:获取支付接口所需参数
	 * @param data
	 * @return
	 */
	@RequestMapping(value = "/payOrder", method = RequestMethod.POST, consumes = "application/json")
	public JSONObject payOrder(@RequestBody JSONObject data) {
		JSONObject result = new JSONObject();

		String orderId  = data.getString("orderId");

		if(StringUtils.isEmpty(orderId)){
			result.put("status", 500);
			result.put("msg", "订单编号为空,无法进行支付,请联系客服!");
			result.put("data", null);
			return result;
		}

		OrderMall orderMall=orderService.getOrder(orderId);

		if(orderMall==null){
			result.put("status", 500);
			result.put("msg", "此订单不存在,无法进行支付,请联系客服!");
			result.put("data", null);
			return result;
		}


		//判断订单生效时间是否超过30分钟

		if(new Date().getTime()-orderMall.getEffectiveTime().getTime()>0){
			result.put("status", 500);
			result.put("msg", "订单生效时间已超过30分钟,请重新下单!");
			result.put("data", null);
			return result;
		}

		int money=orderMall.getMoney();

		String title = data.getString("commodityName");
		String openId = data.getString("openId");

		try {
			OrderInfo wxPayOrder = new OrderInfo();
			wxPayOrder.setAppid(appId);
			wxPayOrder.setMch_id(mchId);
			wxPayOrder.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
			wxPayOrder.setBody(title);
			wxPayOrder.setOut_trade_no(orderId);
			wxPayOrder.setTotal_fee(money); // 该金钱其实10 是 0.1元
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


				// 业务逻辑结束
				result.put("status", 200);
				result.put("msg", "支付环境生成成功!");
				result.put("data", payInfo);

				return result;
			}
			result.put("status", 500);
			result.put("msg", "支付环境生成失败!");
			result.put("data", null);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}


	/**
	 * 获取用户订单列表数据
	 * @param userId
	 * @param search
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = "/orderlist/{userId}", method = RequestMethod.GET, consumes = "application/json")
	public JSONObject getOrderList(@PathVariable("userId")String userId,@RequestParam("search") String search,
								   @RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize) {

		return orderService.selectOrderListByUserId(userId, pageNum, pageSize);

	}

	/**
	 * 取消订单操作
	 * 说明:1.订单未支付情况下可直接取消订单
	 * 		2.订单已支付,在支付完成30分钟内可以取消订单,超出时间不可进行取消订单操作
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/cancel/{orderId}", method = RequestMethod.POST, consumes = "application/json")
	public JSONObject cancelOrder(@PathVariable("orderId")String orderId,@RequestBody JSONObject data) {
		JSONObject resultJson=new JSONObject();
		OrderMall orderMall=new OrderMall();
		orderMall.setUserId(data.getString("userId"));
		orderMall.setOrderId(orderId);
		//获取此订单最新状态
		OrderMall order=orderService.getOrder(orderId);
		//订单状态
		if(order!=null && order.getOrderStatus().equals("0")){
			orderMall.setOrderStatus(ORDER_CANCEL_STATUS);
			boolean uptReturn=orderService.updateOrder(orderMall);
			if(uptReturn){
				resultJson.put("status",200);
				resultJson.put("msg","取消订单成功");
				resultJson.put("data",null);
			}else{
				resultJson.put("status",500);
				resultJson.put("msg","取消订单失败,请联系客服");
				resultJson.put("data",null);
			}
		}

		if(order!=null && order.getOrderStatus().equals("1")){
			//判断取消时间是否距离支付时间30分钟
			int duration= (int) ((new Date().getTime()-order.getPayTime().getTime())/(1000));

			if(duration>ORDER_CANCEL_DURATION){
				resultJson.put("status",500);
				resultJson.put("msg","支付完成30分钟后无法取消订单,若要取消,请联系客服");
				resultJson.put("data",null);
			}else{
				//已支付取消订单,返回用户金币数
				orderMall.setOrderStatus(ORDER_CANCEL_STATUS);
				boolean uptReturn=orderService.updateOrder(orderMall);
				if(uptReturn){
					int coin=order.getCoin();
					User user=userService.getUserInfo(order.getUserId());
					user.setChipCount(user.getChipCount()+coin);
					uptReturn=userService.modifyUser(user)>0;
				}
				if(uptReturn){
					resultJson.put("status",200);
					resultJson.put("msg","取消订单成功");
					resultJson.put("data",null);
				}else{
					resultJson.put("status",500);
					resultJson.put("msg","取消订单失败,请联系客服");
					resultJson.put("data",null);
				}

			}
		}

		return resultJson;

	}

	/**
	 * 删除订单操作
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/delOrder/{orderId}", method = RequestMethod.POST, consumes = "application/json")
	public JSONObject deleteOrder(@PathVariable("orderId")String orderId) {
		JSONObject resultJson=new JSONObject();
		OrderMall orderMall=new OrderMall();
		orderMall.setDelFlag("1");
		orderMall.setOrderId(orderId);

		boolean uptReturn=orderService.updateOrder(orderMall);
		if(uptReturn){
			resultJson.put("status",200);
			resultJson.put("msg","删除订单成功");
			resultJson.put("data",null);
		}else{
			resultJson.put("status",500);
			resultJson.put("msg","删除订单失败,请联系客服");
			resultJson.put("data",null);
		}


		return resultJson;

	}


	/**
	 * 确认收货操作(手动操作)
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/confirm/{orderId}", method = RequestMethod.POST, consumes = "application/json")
	public JSONObject confirmOrder(@PathVariable("orderId")String orderId,@RequestBody JSONObject data) {
		JSONObject resultJson=new JSONObject();
		OrderMall orderMall=new OrderMall();
		orderMall.setUserId(data.getString("userId"));
		orderMall.setOrderId(orderId);
		//获取此订单最新状态
		OrderMall order=orderService.getOrder(orderId);
		//订单状态更新为已完成
		if(order!=null && order.getOrderStatus().equals("2")){
			orderMall.setOrderStatus(ORDER_CONFIRM_STATUS);
			boolean uptReturn=orderService.updateOrder(orderMall);
			if(uptReturn){
				resultJson.put("status",200);
				resultJson.put("msg","确认收获成功");
				resultJson.put("data",null);
			}else{
				resultJson.put("status",500);
				resultJson.put("msg","确认收获失败,请联系客服");
				resultJson.put("data",null);
			}
		}



		return resultJson;

	}




	/**
	 * 获取订单详情
	 * @param orderId
	 * @return
	 */
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET, consumes = "application/json")
	public Map<String,String> getOrderDetail(@PathVariable("orderId")String orderId) {
		//获取订单详情
		Map<String,String> orderDetail=orderService.getOrderDetail(orderId);
		return orderDetail;
	}


	/**
	 * 微信小程序支付成功回调函数
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/weixin/callback")
	public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
			String sign = PayUtil.sign(validStr, key, "utf-8").toUpperCase();//拼装生成服务器端验证的签名
			// 因为微信回调会有八次之多,所以当第一次回调成功了,那么我们就不再执行逻辑了

			//根据微信官网的介绍，此处不仅对回调的参数进行验签，还需要对返回的金额与系统订单的金额进行比对等
			if(sign.equals(map.get("sign"))){
				/**此处添加自己的业务逻辑代码start**/
				//更新订单状态
				OrderMall orderMall=new OrderMall();
				String outTradeNo=(String) map.get("out_trade_no");
				orderMall.setOrderId(outTradeNo);
				orderMall.setPayTime(new Date());
				orderMall.setOrderStatus(ORDER_PAYED_STATUS);

				//清算订单
				orderService.settleAccounts(orderMall,outTradeNo);

				/**此处添加自己的业务逻辑代码end**/

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
