package com.ascrm.weixpay.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.ascrm.weixpay.api.WeichatPayOrderAPI;
import com.ascrm.weixpay.conf.WeichatPayConfigure;

/***
 * 
 * @Class 	PayToolUtil.java
 * @Author 	作者姓名:Liuxing
 * @Version	1.0
 * @Date	创建时间：2018年9月28日 下午4:50:28
 * @Copyright Copyright by Liuxing
 * @Direction 类说明					支付工具处理类
 */
public class PayToolUtil {

	private static Logger logger = Logger.getLogger( WeichatPayOrderAPI.class ) ;
	
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
     * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 
     * @return boolean 
     */  
    public static boolean isTenpaySign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {  
        StringBuffer sb = new StringBuffer();  
        Set es = packageParams.entrySet();  
        Iterator it = es.iterator();  
        while(it.hasNext()) {  
            Map.Entry entry = (Map.Entry)it.next();  
            String k = (String)entry.getKey();  
            String v = (String)entry.getValue();  
            if(!"sign".equals(k) && null != v && !"".equals(v)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }  
          
        sb.append("key=" + API_KEY);  
          
        //算出摘要  
        String mysign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toLowerCase();  
        String tenpaySign = ((String)packageParams.get("sign")).toLowerCase();  
        
        //System.out.println(tenpaySign + "    " + mysign);  
        return tenpaySign.equals(mysign);  
    }  
  
    /** 
     * @author 
     * @date 2016-4-22 
     * @Description：sign签名 
     * @param characterEncoding 
     *            编码格式 
     * @param parameters 
     *            请求参数 
     * @return 
     */  
    public static String createSign(String characterEncoding, SortedMap<Object, Object> packageParams, String API_KEY) {  
        StringBuffer sb = new StringBuffer();  
        Set es = packageParams.entrySet();  
        Iterator it = es.iterator();  
        while (it.hasNext()) {  
            Map.Entry entry = (Map.Entry) it.next();  
            String k = (String) entry.getKey();  
            String v = (String) entry.getValue();  
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {  
                sb.append(k + "=" + v + "&");  
            }  
        }  
        sb.append("key=" + API_KEY);  
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();  
        return sign;  
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
     * @author 
     * @date 2016-4-22 
     * @Description：将请求参数转换为xml格式的string 
     * @param parameters 
     *            请求参数 
     * @return 
     */  
    public static String getRequestXml(SortedMap<Object, Object> parameters) {  
        StringBuffer sb = new StringBuffer();  
        sb.append("<xml>");  
        Set es = parameters.entrySet();  
        Iterator it = es.iterator();  
        while (it.hasNext()) {  
            Map.Entry entry = (Map.Entry) it.next();  
            String k = (String) entry.getKey();  
            String v = (String) entry.getValue();  
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {  
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");  
            } else {  
                sb.append("<" + k + ">" + v + "</" + k + ">");  
            }  
        }  
        sb.append("</xml>");  
        return sb.toString();  
    }  
  
    /** 
     * 取出一个指定长度大小的随机正整数. 
     *  
     * @param length 
     *            int 设定所取出随机数的长度。length小于11 
     * @return int 返回生成的随机数。 
     */  
    public static int buildRandom(int length) {  
        int num = 1;  
        double random = Math.random();  
        if (random < 0.1) {  
            random = random + 0.1;  
        }  
        for (int i = 0; i < length; i++) {  
            num = num * 10;  
        }  
        return (int) ((random * num));  
    }  
  
    /** 
     * 获取当前时间 yyyyMMddHHmmss 
     *  
     * @return String 
     */  
    public static String getCurrTime() {  
        Date now = new Date();  
        SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");  
        String s = outFormat.format(now);  
        return s;  
    }  
	
    /**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     *
     * @param responseString API返回的XML数据字符串
     * @return API签名是否合法
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static boolean checkIsSignValidFromResponseString(String responseString) {

        try {
        	SortedMap<String, Object> map = XMLUtil4jdom.doXMLParse( responseString );
        	//SortedMap<String, Object> map = XMLUtil.doXMLParse(responseString);
            //Map<String, Object> map = XMLParser.getMapFromXML(responseString);
            logger.debug(map.toString());
            String signFromAPIResponse = map.get("sign").toString();
            if ("".equals(signFromAPIResponse) || signFromAPIResponse == null) {
                logger.debug("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
                return false;
            }
            logger.debug("服务器回包里面的签名是:" + signFromAPIResponse);
            //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
            map.put("sign", "");
            //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
            String signForAPIResponse = PayCommonUtil.createSign("UTF-8", map);
            if (!signForAPIResponse.equals(signFromAPIResponse)) {
                //签名验不过，表示这个API返回的数据有可能已经被篡改了
                logger.debug("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
                return false;
            }
            logger.debug("恭喜，API返回的数据签名验证通过!!!");
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
