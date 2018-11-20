package com.pokepet.algorithm;
 
import org.apache.commons.codec.binary.Base64;
 
import net.sf.json.JSONObject;
 
 
/**
 * 封装对外访问方法
 * @author liuyazhuang
 *
 */
public class WXCore {
	
	private static final String WATERMARK = "watermark";
	private static final String APPID = "appid";
	/**
	 * 解密数据
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String appId, String encryptedData, String sessionKey, String iv){
		String result = "";
		try {
			AES aes = new AES();  
		    byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
		    if(null != resultByte && resultByte.length > 0){
		        result = new String(WxPKCS7Encoder.decode(resultByte));
		    	JSONObject jsonObject = JSONObject.fromObject(result);
		    	String decryptAppid = jsonObject.getJSONObject(WATERMARK).getString(APPID);
		    	if(!appId.equals(decryptAppid)){
		    		result = "";
		    	}
	        }
		} catch (Exception e) {
			result = "";
			e.printStackTrace();
		}
	    return result;
	}
	
	
	public static void main(String[] args) throws Exception{
	   String appId = "wx21a19c3555f7da35";
	   String encryptedData ="vNDUCSHTI8iR+iLXVQlKWaBGyb9lJmblzfcHRsihYuVPaxCXarUBxdgpO/smtQFUdTUzFxrgc/aBNkDs1Ndufgo/4Bw2O7hcVKRSLjjzr1iscLJmPn7P/LJavs2675S70+rjHpPiCHBSDuir82zM2dERiEaUNCnUCozaTU2RwXQ5I2deS/Srnll5OznAvRFKv+B5ATvUuFqEEHfL6cqV5G354Y0Ar9xT+fXAw2qnO0EkvKXtXIDPM0NRnlux5ZlMc7+7sBjMNAoUpQShk4l8CjUFG4fbp3+5JHWlbuJAuji/yhwehZLusnAZH5KjTXHPFqShHpoW33amZe2WBSLfP82bzngqm47/fo4KcJY2v+woyKcRJ5KmABfuARUDZWoH8aoxt4ESqspahAV3XSpnAjqBo0vQanPhSbUHiHA0OQSD/rDMAyqG00pHfksdx4DzOY/Y/FEAeRcEo7MwQMRhnjLSa5HBAKC3ZjkhW6Eq1RS1eh4qYHZXptm+WXViCt3M";
		String sessionKey = "Ob095+h8TM+9SsRRAzV9Xg==";
	   String iv ="XiynJ2gvt75OwqYxhoI0Vw==";
       System.out.println(decrypt(appId, encryptedData, sessionKey, iv));
    }
}