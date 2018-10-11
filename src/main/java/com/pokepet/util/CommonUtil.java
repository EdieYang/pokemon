package com.pokepet.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * 
 * ClassName: CommonUtil 
 * @Description: 工具类
 * @author Bean Zhou
 * @date 2018年5月25日
 */
public class CommonUtil {
	
	/**
	 * 
	 * @Description: 获取年龄
	 * @param @param birthday
	 * @param @return   
	 * @return int  
	 * @throws
	 * @author Bean Zhou
	 * @Modified By lily
	 * @date 2018年5月25日
	 */
	public static String getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			return "";
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthday);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		//获取
		int year = yearNow - yearBirth;
		int month=monthNow-monthBirth;
		int day=dayOfMonthNow-dayOfMonthBirth;

		if(day<0){
			month -= 1;
			cal.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数。
			day = day + cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		}
		if(month<0){
			month = (month+12)%12;
			year--;
		}
		System.out.println("年龄："+year+"年"+month+"月"+day+"天");
		double ageD=year+month/12.0+	day/30.0*0.1;
		DecimalFormat df = new DecimalFormat("0.00");
		String age=df.format(ageD);
		System.out.println(age);

		return age;
	}


	/**
	 * 获取请求ip地址
	 * @param request
	 * @return
     */
	public static String getRemoteHost(javax.servlet.http.HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}



	/**
	 *
	 * 方法用途: 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序），并且生成url参数串<br>
	 * 实现步骤: <br>
	 *
	 * @return
	 */
	public static String formatUrlMap(Map<Object, Object> parameters,String key){
		StringBuffer sb =new StringBuffer();

		if (!(parameters instanceof SortedMap<?,?>)) {

			parameters = new TreeMap<Object,Object>(parameters);

		}

		Set<?>es =parameters.entrySet();//所有参与传参的参数按照accsii排序（升序），sign参数不参与签名

		Iterator<?>it =es.iterator();

		while (it.hasNext()) {

			Map.Entry entry= (Map.Entry)it.next();

			String k = (String)entry.getKey();

			Object v =entry.getValue();

			if (null !=v && !"".equals(v) && !"sign".equals(k)

					&&!"key".equals(k)) {

				sb.append(k +"=" +v +"&");

			}

		}

		sb.append("key=" +key);
		String sign=EncoderByMd5(sb.toString()).toUpperCase();

		return sign;




	}





	/**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static  Map<String,String> doXMLParse(String strxml) throws Exception {
		if(null == strxml || "".equals(strxml)) {
			return null;
		}

		Map<String,String> m = new HashMap<String,String>();
		InputStream in = String2Inputstream(strxml);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if(children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}

			m.put(k, v);
		}

		//关闭流
		in.close();

		return m;
	}

	public  static InputStream String2Inputstream(String str) {
		return new ByteArrayInputStream(str.getBytes());
	}

	/**
	 * 获取子结点的xml
	 * @param children
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if(!children.isEmpty()) {
			Iterator it = children.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if(!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
	}






	/**
	 * 方法名: getRemotePortData
	 * 描述: 发送远程请求 获得代码示例
	 * 参数：  @param urls 访问路径
	 * 参数：  @param param 访问参数-字符串拼接格式, 例：port_d=10002&port_g=10007&country_a=
	 * 创建人: Xia ZhengWei
	 * 创建时间: 2017年3月6日 下午3:20:32
	 * 版本号: v1.0
	 * 返回类型: String
	 */
	public static String getRemotePortData(String urls, String param){
		try {
			URL url = new URL(urls);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置连接超时时间
			conn.setConnectTimeout(30000);
			// 设置读取超时时间
			conn.setReadTimeout(30000);
			conn.setRequestMethod("POST");
			if(StringUtils.isNotEmpty(param)) {
				conn.setRequestProperty("Origin", "https://sirius.searates.com");// 主要参数
				conn.setRequestProperty("Referer", "https://sirius.searates.com/cn/port?A=ChIJP1j2OhRahjURNsllbOuKc3Y&D=567&G=16959&shipment=1&container=20st&weight=1&product=0&request=&weightcargo=1&");
				conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");// 主要参数
			}
			// 需要输出
			conn.setDoInput(true);
			// 需要输入
			conn.setDoOutput(true);
			// 设置是否使用缓存
			conn.setUseCaches(false);
			// 设置请求属性
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
			conn.setRequestProperty("Charset", "UTF-8");

			if(StringUtils.isNotEmpty(param)) {
				// 建立输入流，向指向的URL传入参数
				DataOutputStream dos=new DataOutputStream(conn.getOutputStream());
				dos.writeBytes(param);
				dos.flush();
				dos.close();
			}
			// 输出返回结果
			InputStream input = conn.getInputStream();
			int resLen =0;
			byte[] res = new byte[1024];
			StringBuilder sb=new StringBuilder();
			while((resLen=input.read(res))!=-1){
				sb.append(new String(res, 0, resLen));
			}
			return sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}



	public static String httpsRequest(String requestUrl, String requestMethod, String output) {
		try{
			URL url = new URL(requestUrl);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod(requestMethod);
			if (null != output) {
				OutputStream outputStream = connection.getOutputStream();
				outputStream.write(output.getBytes("UTF-8"));
				outputStream.close();
			}
			// 从输入流读取返回内容
			InputStream inputStream = connection.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			inputStream.close();
			inputStream = null;
			connection.disconnect();
			return buffer.toString();
		}catch(Exception ex){
			ex.printStackTrace();
		}

		return "";
	}






	public static String EncoderByMd5(String str) {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
