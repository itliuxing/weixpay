package com.ascrm.weixpay.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.ascrm.weixpay.conf.WeichatPayConfigure;
import com.ascrm.weixpay.constant.unified.UnifiedOrderRequestData;
import com.ascrm.weixpay.constant.unified.UnifiedOrderResponseData;


/**
 * 
 * @author wangkai
 * @2016年5月31日 下午8:34:54
 * @desc:微信支付随机字符串和签名工具
 */
public class PayCommonUtil {
	private static Logger logger = LoggerFactory.getLogger(PayCommonUtil.class);
	
	/**
	 * 自定义长度随机字符串
	 * @param length
	 * @return
	 */
	public static String createConceStr(int length) {
		String strs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String str = "";
		for (int i = 0; i < length; i++) {
			// str +=strs.substring(0, new Random().nextInt(strs.length()));
			char achar = strs.charAt(new Random().nextInt(strs.length() - 1));
			str += achar;
		}
		return str;
	}
    
	/**
	 * 默认16 位随机字符串
	 * @return
	 */
	public static String CreateNoncestr() {
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		String res = "";
		for (int i = 0; i < 16; i++) {
			Random rd = new Random();
			res += chars.charAt(rd.nextInt(chars.length() - 1));
		}
		return res;
	}

	/**
	 * 签名工具
	 * @author wangkai
	 * @date 2014-12-5下午2:29:34
	 * @Description：sign签名
	 * @param characterEncoding
	 *            编码格式 UTF-8
	 * @param parameters
	 *            请求参数
	 * @return
	 */
	public static String createSign(String characterEncoding,
			Map<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, Object>> it = parameters.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry <String,Object>entry = (Map.Entry<String,Object>) it.next();
			String key = (String) entry.getKey();
			Object value = entry.getValue();//去掉带sign的项
			if (null != value && !"".equals(value) && !"sign".equals(key)
					&& !"key".equals(key)) {
				sb.append(key + "=" + value + "&");
			}
		}
		sb.append("key=" + WeichatPayConfigure.API_KEY );
		//注意sign转为大写
		return MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
	}
	
	/**
	 * 签名工具 FOR PUBLIBC
	 * @param characterEncoding
	 * @param parameters
	 * @return
	 */
	public static String createSignPublic(String characterEncoding,
			Map<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, Object>> it = parameters.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry <String,Object>entry = (Map.Entry<String,Object>) it.next();
			String key = (String) entry.getKey();
			Object value = entry.getValue();//去掉带sign的项
			if (null != value && !"".equals(value) && !"sign".equals(key)
					&& !"key".equals(key)) {
				sb.append(key + "=" + value + "&");
			}
		}
		sb.append("key=" + WeichatPayConfigure.API_KEY);
		//注意sign转为大写
		return MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
	}
	
	/**
	 * 签名工具 不含商户密钥 －暂时不用
	 * @param characterEncoding
	 *        编码格式 UTF-8
	 * @param parameters
	 * @return
	 */
	public static String createSignNoKey(String characterEncoding,
			Map<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, Object>> it = parameters.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry <String,Object>entry = (Map.Entry<String,Object>) it.next();
			String key = (String) entry.getKey();
			Object value = entry.getValue();//去掉带sign的项
			if (null != value && !"".equals(value) && !"sign".equals(key)
					&& !"key".equals(key)) {
				sb.append(key + "=" + value + "&");
			}
		}
		String signStr = sb.toString();
		String subStr = signStr.substring(0, signStr.length()-1);
		//注意sign转为大写
		return MD5Util.MD5Encode(subStr, characterEncoding).toUpperCase();
	}

	/**
	 * @author wangkai
	 * @date
	 * @Description：将请求参数转换为xml格式的string
	 * @param parameters
	 *            请求参数
	 * @return
	 */
	public static String getRequestXml(SortedMap<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Iterator<Entry<String, Object>> iterator = parameters.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,Object> entry = (Map.Entry<String,Object>) iterator.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(key) || "body".equalsIgnoreCase(key)
					|| "sign".equalsIgnoreCase(key)) {
				sb.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
			} else {
				sb.append("<" + key + ">" + value + "</" + key + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * @author wangkai
	 * @date
	 * @Description：返回给微信的参数
	 * @param return_code
	 *            返回编码
	 * @param return_msg
	 *            返回信息
	 * @return
	 */
	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}
	
	
	/**
     * 从API返回的XML数据里面重新计算一次签名
     *
     * @param responseString API返回的XML数据
     * @return 新鲜出炉的签名
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
	 * @throws JDOMException 
     */
    public static String getSignFromResponseString(String responseString) throws IOException, SAXException, ParserConfigurationException, JDOMException {
    	SortedMap<String, Object> map = XMLUtil4jdom.doXMLParse( responseString );
        //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
        map.put("sign", "");
       // map.remove("sign");
        //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
        return PayCommonUtil.createSign("UTF-8", map);
    }

    


	/**
	 * 利用反射获取Java对象的字段并进行加密，过滤掉sign字段
	 * @param data
	 * @return
	 * return:String
	 */
	public static String getSign(Object data) {
		StringBuilder stringA = new StringBuilder();
		Map<String, String> map = new HashMap<String, String>();
		Field[] fields = data.getClass().getDeclaredFields();
		Method[] methods = data.getClass().getDeclaredMethods();
		for (Field field : fields) {
			String fieldName = field.getName();
			if (field != null && !fieldName.equals("sign")) {
				String getMethodName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
				for (Method method : methods) {
					if (method.getName().equals(getMethodName)) {
						try {
							if (method.invoke(data, new Object[]{}) != null && method.invoke(data, new Object[]{}).toString().length() != 0) {
								map.put(fieldName, method.invoke(data, new Object[]{}).toString());
							}
						} catch (IllegalAccessException | IllegalArgumentException
								| InvocationTargetException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		List<Map.Entry<String,String>> mappingList = null; 
    	//通过ArrayList构造函数把map.entrySet()转换成list 
    	mappingList = new ArrayList<Map.Entry<String,String>>(map.entrySet()); 
    	//通过比较器实现比较排序 
    	Collections.sort(
			mappingList, 
			new Comparator<Map.Entry<String,String>>(){ 
				public int compare(Map.Entry<String,String> mapping1,Map.Entry<String,String> mapping2){ 
					return mapping1.getKey().compareTo(mapping2.getKey()); 
				} 
	  		}
    	);
		for (Map.Entry<String, String> mapping : mappingList) {
			stringA.append("&").append(mapping.getKey()).append("=").append(mapping.getValue());
		}
		String stringSignTemp = stringA.append("&key=").append( WeichatPayConfigure.API_KEY).substring(1);
		return MD5Util.MD5(stringSignTemp).toUpperCase();
	}
    
}
