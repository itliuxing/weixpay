package com.ascrm.weixpay.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.ascrm.weixpay.conf.SystemInfoConf;
import com.ascrm.weixpay.conf.WeichatPayConfigure;
import com.ascrm.weixpay.constant.HandlerResultOut;
import com.ascrm.weixpay.constant.unified.UnifiedOrderRequestData;
import com.ascrm.weixpay.constant.unified.UnifiedOrderResponseData;
import com.ascrm.weixpay.inoutobj.JSPayOrderRequestIn;
import com.ascrm.weixpay.inoutobj.JSPayOrderRequestOut;
import com.ascrm.weixpay.util.PayCommonUtil;
import com.ascrm.weixpay.util.WXSignatureUtil;
import com.ascrm.weixpay.util.WxPayUtil;
import com.ascrm.weixpay.util.common.ObjectUtils;


/**
 * @Class 	WeichatJSPayAPI.java
 * @Author 	作者姓名:刘兴 
 * @Version	1.0
 * @Date	创建时间：2017-10-16 上午11:43:51
 * @Copyright Copyright by 智多星
* @Direction 类说明				生成微信支付订单		场景:H5 支付
 */

public class WeichatJSPayAPI {
	
	private static Logger logger = LoggerFactory.getLogger( WeichatJSPayAPI.class ) ;
	
	/**
	 * 微信预支付
	 * @param orderIn
	 * @return
	 */
	public HandlerResultOut receipt( JSPayOrderRequestIn orderIn ) {
		// 日记记录
		StringBuilder jsapiInfo = new StringBuilder() ;
		jsapiInfo.append("jsapi支付下订单,用户ID：").append(orderIn.getUserid()) ;
		jsapiInfo.append(" OPENID：").append(orderIn.getOpenID() ) ;
		jsapiInfo.append(" IP：").append(orderIn.getCreateIp() ) ;
		jsapiInfo.append(" 订单号：").append(orderIn.getPorOutTradeNo() ) ;
		jsapiInfo.append(" 订单产品：").append(orderIn.getPorGoodTitle() ) ;
		jsapiInfo.append(" 金额：").append(orderIn.getPorOrderAmount() ).append( " 分" ) ;
		logger.info( jsapiInfo.toString() );
		try {
			boolean isNUll = ObjectUtils.allfieldIsNUll( orderIn );
			if (isNUll) {
				return HandlerResultOut.fail( SystemInfoConf.param_null_code ) ;
			}
		} catch (Exception e) {
			logger.info( "数据验证出现异常....." );
			return HandlerResultOut.fail( SystemInfoConf.param_null_code ) ;
		}
		try {
			//调用统一下单接口
			UnifiedOrderResponseData responseData = unifiedOrder( orderIn );
			//生成web前端可用数据
			return getAppPackage( responseData , orderIn.getPorOutTradeNo() );
		} catch (Exception e) {
			StringBuilder errormsg = new StringBuilder() ;
			errormsg.append( "receipt(String openid,String proId,String ip)：{},{}" );
			jsapiInfo.append(" 订单号：").append(orderIn.getPorOutTradeNo() ) ;
			jsapiInfo.append(" 订单产品：").append(orderIn.getPorGoodTitle() ) ;
			jsapiInfo.append(" IP：").append(orderIn.getCreateIp() ).append(" 异常 ").append(e.getMessage());
			logger.error( errormsg.toString() );
			return HandlerResultOut.fail( SystemInfoConf.order_pay_jsapi, errormsg.toString() ) ;
		}
	}
	
	/***
	 * 返回调用JSAPI的端参数信息
	 * @param responseData		统一下单姐姐口返回数据集
	 * @param OutTradeNo		外部系统订单号，其实就是本系统产生的订单号
	 * @return
	 */
	public HandlerResultOut getAppPackage( UnifiedOrderResponseData responseData , String OutTradeNo )  {
		//写出H5端需要请求的数据-----生成那个校验的信息码
		JSPayOrderRequestOut payRequest = new JSPayOrderRequestOut() ;
		payRequest.setPrepayId( responseData.getPrepay_id()  ) ;
		payRequest.setAppId(  responseData.getAppid() ) ;
		payRequest.setSignType( "MD5" ) ;
		payRequest.setNonceStr( responseData.getNonce_str() ) ;
		payRequest.setTimeStamp( System.currentTimeMillis() ) ;
		//生成码MD5  String valiSign = SIGNATURE.sign( result );
		//MD5加密请求数据
		String valiSign = WXSignatureUtil.wxJsapiSignature( responseData.getAppid() , payRequest.getTimeStamp() ,
				payRequest.getNonceStr() , payRequest.getPrepayId() , payRequest.getSignType() , WeichatPayConfigure.API_KEY ) ; 
		payRequest.setPaySign( valiSign) ;
		logger.info( new StringBuilder("生成统一支付订单成功后生成H5请求数据信息.").append(valiSign).toString() );
		//商户系统内部订单号
		payRequest.setPayOrderID( OutTradeNo ) ;
		return HandlerResultOut.success(payRequest) ;
	}
	

	/**
	 * 调用微信统一下单接口,返回客户端数据
	 * 
	 * @param tradeType
	 *            JSAPI支付
	 * @return UnifiedOrderResponseData
	 */
	private UnifiedOrderResponseData unifiedOrder( JSPayOrderRequestIn orderIn )throws Exception { // proId 问题id
		// 生成请求数据对象
		UnifiedOrderRequestData data = constructData( orderIn );
		// 调用微信统一下单接口
		UnifiedOrderResponseData responseData = WxPayUtil.unifiedOder(data);
		logger.info("UnifiedOrderResponseData => " + JSONObject.toJSONString(responseData));
		return responseData;
	}
	

	/**
	 * 构建统一下单参数，发给微信服务器
	 * @param orderIn
	 * @return
	 */
	private UnifiedOrderRequestData constructData( JSPayOrderRequestIn orderIn ) {
		UnifiedOrderRequestData data = new UnifiedOrderRequestData.
			UnifiedOrderReqDataBuilder( orderIn.getPorGoodTitle() , orderIn.getPorOutTradeNo() , 
					orderIn.getPorOrderAmount() , orderIn.getCreateIp() ,
			WeichatPayConfigure.TRADE_TYPE_JSAPI).setOpenid( orderIn.getOpenID() ).build();
		// 产生签名信息
		data.setSign(PayCommonUtil.getSign(data));
		return data;
	}
	
	
	//------------------------------------------------------------------------------------------------------------------------

	/***
	 * 微信下单生成预付单
	 * @param OrderepayRequest
	 * @return
	 * @throws WeixinException 
	 * @throws IntrospectionException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * version 1.0 版本，基本只做参考作用
	 */
	/*public static WebServiceOutData requestOrderInfoWX(JSPayOrderRequestIn OrderepayRequest 
			,WebServiceOutData webServiceOutData ) throws WeixinException {
		//WeixinPayAccount weixinAccount = new WeixinPayAccount( OrderepayRequest.getWechatID(), OrderepayRequest.getWechatKEY() , OrderepayRequest.getWechatMchId()) ;
		//PayApi wxPayAPI = new PayApi( ACCOUNT ) ;
		//统一下单--再生成jsapi请求数据
		MchPayPackage payPackage = new MchPayPackage(
				OrderepayRequest.getPorGoodBody() , 				//商品简单描述
				OrderepayRequest.getPorOutTradeNo() ,  				//商户系统内部订单号
				(OrderepayRequest.getPorOrderAmount() ) ,			//支付金额（分）
				OrderepayRequest.getSuccessUrl() , 					//回调地址
				OrderepayRequest.getCreateIp(), 					//请求IP地址
				TradeType.MICROPAY ,								//支付方式
				OrderepayRequest.getOpenID() , //"ovayHw_2hpIw38gjxV7vyyM_YjbI" , 		//OPENID 支付端的openid
				null , 				//
				null , 
				"支付测试"								//附加数据		查询时或者回调时返回
				) ;
		
		//MchPayRequest payRequest1 = wxPayAPI.createJSPayRequest(  ACCOUNT.getId(), OrderepayRequest.getPorGoodBody() , OrderepayRequest.getPorOrderId(), 
		//		(OrderepayRequest.getPorOrderAmount() * 100 ), OrderepayRequest.getSuccessUrl() , OrderepayRequest.getCreateIp(), "") ;
		//生成统一支付订单
		PrePay result = PAY.createPrePay(payPackage) ; //wxPayAPI.createPrePay(payPackage);

		logger.info( "当前订单需要支付的金额：" + payPackage.getTotalFee() ) ;
		//统一生成订单信息解析
		Assert.assertEquals(Consts.SUCCESS, result.getReturnCode());
		logger.info( new StringBuilder("生成统一支付订单返回码:").append( result.getReturnCode() ).toString() );
		//生成统一下单成功后====
		if( result.getReturnCode().equals(Consts.SUCCESS) && result.getResultCode().equals(Consts.SUCCESS) ){
			HashMap<String, Object> outMap = new HashMap<String, Object>();
			try {
				//存储下单的信息至数据库---------这是业务系统的事情了
				PayOrderepayRequest payOrderepayRequest = new PayOrderepayRequest() ; 
				BeanUtils.copyProperties( OrderepayRequest, payOrderepayRequest) ;
				payOrderepayRequest.setPorPayType( PayOrderepayRequest.porOutTradeNoS ) ;		//实时支付
				payOrderepayRequest.setPorStatus( PayOrderepayRequest.porStatusI ) ;			//待支付
				payOrderepayRequest.setPorBalanceStatus( PayOrderepayRequest.porBalanceWD ) ;	//待结算
				payOrderepayRequest.setPorGmtCreate( new Date() ) ;
				payOrderepayRequestService.savePayOrderepayRequest( payOrderepayRequest ) ;
				logger.info( new StringBuilder("生成统一支付订单成功后写入支付请求信息表").toString() );
				
				//写出H5端需要请求的数据-----生成那个校验的信息码
				WXBrandWCPayRequest payRequest = new WXBrandWCPayRequest() ;
				payRequest.setPrepayId( result.getPrepayId() ) ;
				payRequest.setAppId(  result.getAppId() ) ;
				payRequest.setSignType( "MD5" ) ;
				payRequest.setNonceStr( result.getNonceStr() ) ;
				payRequest.setTimeStamp( System.currentTimeMillis() ) ;
				//生成码MD5  String valiSign = SIGNATURE.sign( result );
				//MD5加密请求数据
				String valiSign = WXSignatureUtil.wxJsapiSignature( result.getAppId() , payRequest.getTimeStamp() ,
						payRequest.getNonceStr() , payRequest.getPrepayId() , payRequest.getSignType() , ACCOUNT.getPaySignKey() ) ; 
				payRequest.setPaySign( valiSign) ;
				logger.info( new StringBuilder("生成统一支付订单成功后生成H5请求数据信息.").append(valiSign).toString() );
				payRequest.setPayOrderID( OrderepayRequest.getPorOutTradeNo() ) ;
				//返回数据至调用端
				outMap.putAll(BeanToMapUtil.convertBean( payRequest ));
			} catch ( Exception e) {
				e.printStackTrace();
			}
			webServiceOutData.setOutmap( outMap ) ;
			//webServiceOutData.setReturncode( WebserviceUtil.SUCCESS ) ;
		}
		return webServiceOutData ;
	}*/
	
}
