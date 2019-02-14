package com.ascrm.weixpay.api;

import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.ascrm.weixpay.conf.SystemInfoConf;
import com.ascrm.weixpay.conf.WeichatPayConfigure;
import com.ascrm.weixpay.constant.HandlerResultOut;
import com.ascrm.weixpay.constant.WeixinConstant;
import com.ascrm.weixpay.util.PayToolUtil;
import com.ascrm.weixpay.util.XMLUtil4jdom;
import com.ascrm.weixpay.util.common.HttpUtil;

/**
* @Class 	WeichatPayOrderAPI.java
* @Author 	作者姓名:刘兴 
* @Version	1.0
* @Date		创建时间：2018年9月27日 下午4:27:53
* @Copyright Copyright by 刘兴
* @Direction 类说明				微信订单支付结果查询		场景:均可查询 
* 
* 微信开发API：https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_2
*/
public class WeichatPayOrderAPI {

	private static Logger logger = Logger.getLogger( WeichatPayOrderAPI.class ) ;
	

	/**
	 * 封装查询请求数据
	 * @param outTradeNo 		系统订单号
	 * @param transactionId		微信订单号
	 * @param path				数据访问PATH
	 * @return
	 */
	private static SortedMap<String, Object> queryData(String outTradeNo,String transactionId , String path ) throws Exception {
		// 微信支付账号信息 
        String appid = WeichatPayConfigure.APPID	;  			// appid  
        //String appsecret = WeixinPayConfigure.APP_SECRET; // appsecret  
        String mch_id = WeichatPayConfigure.MCH_ID; 			// 商业号  
        String key = WeichatPayConfigure.API_KEY;			// key  
		// 封装查询接口
        SortedMap<Object,Object> queryParams = new TreeMap<Object,Object>();  
		queryParams.put("appid", appid ) ;
		queryParams.put("mch_id", mch_id ) ;
		queryParams.put("nonce_str", PayToolUtil.CreateNoncestr()) ;
		// 微信的订单号，优先使用
		if ( StringUtils.isNotBlank(outTradeNo) ) {
			queryParams.put("out_trade_no", outTradeNo) ;
		} else {
			queryParams.put("transaction_id", transactionId) ;
		}
		// MD5签名
		String sign = PayToolUtil.createSign("UTF-8", queryParams,key);  
		queryParams.put("sign", sign);
		

		String requestXML = PayToolUtil.getRequestXml( queryParams );  
		logger.info( "查询订单请求参数：\n"  + requestXML );  
		
		String responseXml = HttpUtil.postData(path, requestXML );  
		logger.info( "查询订单返回结果：\n" + responseXml);  

		return XMLUtil4jdom.doXMLParse( responseXml );
	}
	
	

	/**
	 * 查询订单状态
	 * @param params
	 * API : https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_2
	 *            订单查询参数
	 * @return
	 */
	public static HandlerResultOut checkOrderStatus(String outTradeNo,String transactionId) throws Exception {
		SortedMap<String, Object> responseMap = queryData(outTradeNo, transactionId ,  WeichatPayConfigure.PAY_QUERY_ORDER_API ) ;
		
		try {
			
			// 校验响应结果return_code----验证失败
			if ( WeixinConstant.FAIL.equalsIgnoreCase(
					responseMap.get("return_code").toString() ) ) {
				String message = "订单查询失败：" + responseMap.get("return_msg")  ; 
				logger.info( message );
				return HandlerResultOut.fail( SystemInfoConf.check_code , message ) ;
			}
			// 校验业务结果result_code-----订单号不存等失败
			if ( WeixinConstant.FAIL.equalsIgnoreCase(
					responseMap.get( "result_code").toString())) {
				String message = "订单查询失败：" + responseMap.get("return_msg")  ; 
				logger.info( message );
				return HandlerResultOut.fail( SystemInfoConf.query_fail , message ) ;
			}
			// 校验签名
			/*if ( !PayToolUtil.checkIsSignValidFromResponseString(responseXml) ) {
				String message = "订单查询失败,签名可能被篡改："+responseXml  ; 
				logger.info( message );
				return HandlerResultOut.fail( SystemInfoConf.query_fail_sign , message ) ;
			}*/
			// 判断支付状态
			String tradeState = responseMap.get("trade_state").toString();
			if (tradeState != null && tradeState.equals("SUCCESS")) {
				logger.info( "订单支付成功....." );
				return HandlerResultOut.success() ;
			} else if (tradeState == null) {
				logger.info("获取订单状态失败");
				return HandlerResultOut.fail( SystemInfoConf.query_fail_order_status ) ;
			} else if (tradeState.equals("REFUND")) {
				logger.info("转入退款");
				return HandlerResultOut.fail( SystemInfoConf.query_order_status_refund ) ;
			} else if (tradeState.equals("NOTPAY")) {
				logger.info("未支付");
				return HandlerResultOut.fail( SystemInfoConf.query_order_status_nopay ) ;
			} else if (tradeState.equals("CLOSED")) {
				logger.info("已关闭");
				return HandlerResultOut.fail( SystemInfoConf.query_order_status_close ) ;
			} else if (tradeState.equals("REVOKED")) {
				logger.info("已撤销（刷卡支付)");
				return HandlerResultOut.fail( SystemInfoConf.query_order_status_close ) ;
			} else if (tradeState.equals("USERPAYING")) {
				logger.info("用户支付中");
				return HandlerResultOut.fail( SystemInfoConf.query_order_status_paying ) ;
			} else if (tradeState.equals("PAYERROR")) {
				logger.info("支付失败");
				return HandlerResultOut.fail( SystemInfoConf.query_order_status_error ) ;
			}
			return HandlerResultOut.fail( SystemInfoConf.query_order_status_error ) ;
		} catch (Exception e) {
			String message = "订单查询失败,查询参数 = {}   ---  " + e.getMessage()  ; 
			logger.error( message );
			return HandlerResultOut.fail( SystemInfoConf.query_order_status_error ) ;
		}
	}
	
	/**
	 * 关闭订单
	 * @param packageParams
	 * API : https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_3
	 * @return
	 */
	public static HandlerResultOut closeOrder( String outTradeNo ) throws Exception {
		SortedMap<String, Object> responseMap = queryData(outTradeNo, null , WeichatPayConfigure.PAY_CLOSE_ORDER_API ) ;

		try {
		    // 校验响应结果return_code----验证失败
 			if ( WeixinConstant.FAIL.equalsIgnoreCase(
 					responseMap.get("return_code").toString() ) ) {
 				String message = "关闭订单失败：" + responseMap.get("return_msg")  ; 
 				logger.info( message );
 				return HandlerResultOut.fail( SystemInfoConf.close_order_closed , message ) ;
 			}else if ( WeixinConstant.SUCCESS.equalsIgnoreCase(
 					responseMap.get("return_code").toString() ) ){
 				// 最新的版本----->>>>>> 每次都是成功，第一次成功就关闭订单了
 				if ( WeixinConstant.SUCCESS.equalsIgnoreCase(
 	 					responseMap.get("return_code").toString() ) ){
 					return HandlerResultOut.success() ;
 				}else
				// 校验业务结果result_code-----订单号不存等失败
				if ( WeixinConstant.FAIL.equalsIgnoreCase(
						responseMap.get( "result_code").toString())) {
					// 校验签名
				/*	if ( !PayToolUtil.checkIsSignValidFromResponseString(responseXml) ) {
						String message = "关闭订单失败,签名可能被篡改："+responseXml  ; 
						logger.info( message );
						return HandlerResultOut.fail( SystemInfoConf.query_fail_sign , message ) ;
					}*/
					// 判断关闭状态
					String tradeState = responseMap.get("trade_state").toString();
					if ( StringUtils.isBlank( tradeState ) ){
						logger.info( "未获取到数据，关闭失败." );
						return HandlerResultOut.fail( SystemInfoConf.close_order_error ) ;
					} else if (tradeState.equals("ORDERPAID"))  {
						logger.info("关闭订单-订单已支付，不能发起关单");
						return HandlerResultOut.fail( SystemInfoConf.close_order_payed ) ;
					} else if (tradeState.equals("SYSTEMERROR")) {
						logger.info("关闭订单-系统异常，请重新调用该API");
						return HandlerResultOut.fail( SystemInfoConf.close_order_error ) ;
					} else if (tradeState.equals("ORDERCLOSED")) {
						logger.info("关闭订单-订单已关闭，无需继续调用");
						return HandlerResultOut.fail( SystemInfoConf.close_order_closed ) ;
					} else if (tradeState.equals("SIGNERROR")) {
						logger.info("关闭订单-请检查签名参数和方法是否都符合签名算法要求)");
						return HandlerResultOut.fail( SystemInfoConf.close_order_sign ) ;
					} else if (tradeState.equals("REQUIRE_POST_METHOD")) {
						logger.info("关闭订单-请检查请求参数是否通过post方法提交");
						return HandlerResultOut.fail( SystemInfoConf.close_order_post ) ;
					} else if (tradeState.equals("XML_FORMAT_ERROR")) {
						logger.info("关闭订单-请检查XML参数格式是否正确");
						return HandlerResultOut.fail( SystemInfoConf.close_order_xml ) ;
					}
				}
 			}
			return HandlerResultOut.fail( SystemInfoConf.close_order_error ) ;
		} catch (Exception e) {
			String message = "订单查询失败,查询参数 = {}   ---  " + e.getMessage()  ; 
			logger.error( message );
			return HandlerResultOut.fail( SystemInfoConf.close_order_error , message ) ;
		}
	}
}
