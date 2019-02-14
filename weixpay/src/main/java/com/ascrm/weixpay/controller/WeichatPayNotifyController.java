package com.ascrm.weixpay.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ascrm.weixpay.conf.SystemInfoConf;
import com.ascrm.weixpay.constant.HandlerResultOut;
import com.ascrm.weixpay.constant.WeixinConstant;
import com.ascrm.weixpay.util.PayToolUtil;
import com.ascrm.weixpay.util.XMLUtil4jdom;

/**
 * 
 * @Class 	WeichatPayNotifyController.java
 * @Author 	作者姓名:Liuxing
 * @Version	1.0
 * @Date	创建时间：2018年9月29日 上午17:33:55
 * @Copyright Copyright by Liuxing
 * @Direction 类说明							可以照搬使用，但是未测试，可能里面还有BUG
 * 					微信扫一扫支付成功回调
 * 						API：https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=9_7&index=8
 * 
* 					模拟回调-->调用
* 						二维码获取：http://localhost:8080/weichat_n/notify
 */
@Controller
@RequestMapping(value="/weichat_n")
public class WeichatPayNotifyController  {
	
	private static Logger logger = LoggerFactory.getLogger( WeichatPayNotifyController.class ) ;
	
	/**
	 * 微信平台发起的回调方法，
	 * 调用我们这个系统的这个方法接口，将扫描支付的处理结果告知我们系统
	 * @throws Exception
	 */
	@PostMapping(value = "/notify")
    @ResponseBody
	public void weixinNotify(HttpServletRequest request, HttpServletResponse response) throws  Exception{
        /*//读取参数  
        InputStream inputStream ;  
        StringBuffer sb = new StringBuffer();  
        inputStream = request.getInputStream();  
        String s ;  
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));  
        while ((s = in.readLine()) != null){  
            sb.append(s);
        }
        in.close();
        inputStream.close();
  
        //解析xml成map  
        SortedMap<String, Object> map = XMLUtil4jdom.doXMLParse(sb.toString());  
        
        //过滤空 设置 TreeMap  
        SortedMap<Object,Object> packageParams = new TreeMap<Object,Object>();        
        Iterator it = map.keySet().iterator();  
        while (it.hasNext()) {  
            String parameter = (String) it.next();
            String parameterValue = (String) map.get(parameter);
            
            String v = "";  
            if(null != parameterValue) {
                v = parameterValue.trim();  
            }  
            packageParams.put(parameter, v);  
        }  
          
        // 账号信息  
        String key = WeichatPayConfigure.API_KEY ; //key  
  */
        //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了. 
		HandlerResultOut resultOut = callback(request) ;
		StringBuilder resXml = new  StringBuilder() ;
        // 支付成功  
        if( resultOut.getCode().equals(0) ){   
            resXml.append("<xml>").append("<return_code><![CDATA[SUCCESS]]></return_code>") ;  
            resXml.append("<return_msg><![CDATA[OK]]></return_msg></xml> ");  
        } 
        // 支付失败
        else {
        	resXml.append("<xml>").append("<return_code><![CDATA[FAIL]]></return_code>" ) ; 
        	resXml.append("<return_msg><![CDATA[报文为空]]></return_msg></xml>") ;  
        }
        //------------------------------  
        //处理业务完毕  
        //------------------------------  
        BufferedOutputStream out = new BufferedOutputStream(  
                response.getOutputStream());  
        out.write(resXml.toString().getBytes());  
        out.flush();  
        out.close();  
    }
	

	/**
	 * 微信回调告诉微信支付结果 注意：同样的通知可能会多次发送给此接口，注意处理重复的通知。
	 * 对于支付结果通知的内容做签名验证，防止数据泄漏导致出现“假通知”，造成资金损失。
	 * 
	 * @param params
	 * @return
	 */
	public HandlerResultOut callback( HttpServletRequest request ) {
		try {
			String responseStr = parseWeixinCallback(request);
			SortedMap<String, Object> map = XMLUtil4jdom.doXMLParse( responseStr ) ;
			logger.info("微信支付回调: "+map.toString());
			// 校验签名 防止数据泄漏导致出现“假通知”，造成资金损失
			if (!PayToolUtil.checkIsSignValidFromResponseString(responseStr)) {
				logger.error("微信回调失败,签名可能被篡改 "+responseStr);
				return HandlerResultOut.fail( SystemInfoConf.sign_code );
			}
			if (WeixinConstant.FAIL.equalsIgnoreCase(map.get("result_code")
					.toString())) {
				logger.error("微信回调失败的原因："+responseStr);
				return HandlerResultOut.fail( SystemInfoConf.order_pay_notify );
			}
			if (WeixinConstant.SUCCESS.equalsIgnoreCase(map.get("result_code").toString())) {
				// ----------------------------------------------------------------------------------------------
				
				
				String outTradeNo = (String) map.get("out_trade_no");		//本服务订单号
				String transactionId = (String) map.get("transaction_id");	//微信支付订单号
				String totlaFee = (String) map.get("total_fee");			//微信系统那边支付成功到账的金额
				Double totalPrice = (Double.valueOf(totlaFee)/100) ;		//本系统转换之后的金额 ，对账成功才算通过--可能出现两位小数点之后的数据值
				logger.error("微信回调已支付金额：" + totalPrice + "元");
				logger.error("微信支付进入回调--->> 本系统订单号：" + outTradeNo + " ，可以开始做业务处理了--------");
				
				
				// ----------------------------------------------------------------------------------------------
				logger.info("回调成功："+responseStr);
				return HandlerResultOut.success();
			}
		} catch (Exception e) {
			logger.error("回调异常" + e.getMessage());
		}
		return HandlerResultOut.fail( SystemInfoConf.order_pay_notify_proccess );
	}
	

	/**
	 * 解析微信回调参数
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	private String parseWeixinCallback(HttpServletRequest request){
		// 获取微信调用我们notify_url的返回信息
		String result = "";
		ByteArrayOutputStream outSteam = null ;
		InputStream inStream = null ;
		try {
			inStream = request.getInputStream();
			outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			result = new String(outSteam.toByteArray(), WeixinConstant.UTF_8 );
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(outSteam != null){
					outSteam.close();
					outSteam = null; // help GC
				}
				if(inStream != null){
					inStream.close();
					inStream = null;// help GC
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/*****
	 * 这里放入的是测试数据信息
	 */
	public static void test() {
		/*
		 * 
		
		<xml><appid><![CDATA[wxe6c6ab2ef372xxxx]]></appid>
		<attach><![CDATA[2&85&139&0]]></attach>
		<bank_type><![CDATA[CFT]]></bank_type>
		<cash_fee><![CDATA[1]]></cash_fee>
		<fee_type><![CDATA[CNY]]></fee_type>
		<is_subscribe><![CDATA[Y]]></is_subscribe>
		<mch_id><![CDATA[129933xxxx]]></mch_id>
		<nonce_str><![CDATA[6xj94ajjika3io01f50z2cf9658fhhtj]]></nonce_str>
		<openid><![CDATA[ojN41uHLEXYuHkrJg2_PaDvxxxxx]]></openid>
		<out_trade_no><![CDATA[129933950120170618102333]]></out_trade_no>
		<result_code><![CDATA[SUCCESS]]></result_code>
		<return_code><![CDATA[SUCCESS]]></return_code>
		<sign><![CDATA[5060B8EE326BD346B7808D9996594A79]]></sign>
		<time_end><![CDATA[20170618102338]]></time_end>
		<total_fee>1</total_fee>
		<trade_type><![CDATA[JSAPI]]></trade_type>
		<transaction_id><![CDATA[4001862001201706186249259476]]></transaction_id>
		</xml>
		
		*/
		
		//以上这份测试数据哈，可以使用postman 的post方式，raw选择 application/xml 方式请求测试
	}
	
}
