package com.ascrm.weixpay.util;

import java.util.Map;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ascrm.weixpay.conf.WeichatPayConfigure;
import com.ascrm.weixpay.constant.unified.UnifiedOrderRequestData;
import com.ascrm.weixpay.constant.unified.UnifiedOrderResponseData;
import com.ascrm.weixpay.util.common.BeanToMapUtil;
import com.ascrm.weixpay.util.common.HttpUtil;

/**
* @Class 	WxPayUtil.java
* @Author 	作者姓名:刘兴 
* @Version	1.0
* @Date		创建时间：2018年9月30日 下午2:46:41
* @Copyright Copyright by 刘兴
* @Direction 类说明
*/
public class WxPayUtil {

	private static Logger logger = LoggerFactory.getLogger( WxPayUtil.class ) ;
	
	/**
	 * 调用微信统一下单接口
	 * @param data
	 * @return UnifiedOrderResponseData
	 */
	public static UnifiedOrderResponseData unifiedOder(UnifiedOrderRequestData data)throws Exception {		
		
		SortedMap<Object,Object> packageParams = BeanToMapUtil.convertBean( data ) ;
		String requestXML = PayToolUtil.getRequestXml(packageParams); 
        logger.info( "生成二维码请求参数：" );   
        logger.info( "\n" + requestXML);  
		
		String requestUrl = WeichatPayConfigure.PAY_UNIFIED_ORDER_API;
		String requestMethod = "POST";
		//发送请求
		String responseXml = HttpUtil.postData( WeichatPayConfigure.PAY_UNIFIED_ORDER_API, requestXML );  
        logger.info( "生成统一下单的请求结果：" );   
        logger.info( "\n" + responseXml); 
		//解析响应xml结果数据
        SortedMap<String, Object> map = XMLUtil4jdom.doXMLParse( responseXml );
		UnifiedOrderResponseData responseData = (UnifiedOrderResponseData) BeanToMapUtil.convertMap( UnifiedOrderResponseData.class , map ) ;
		return responseData;
	} 
	
	

}
