package com.ascrm.weixpay.inoutobj;

import java.io.Serializable;

/**
 * @Class 	PayAccountRequest.java
 * @Author 	作者姓名:刘兴 
 * @Version	1.0
 * @Date	创建时间：2017-10-13 上午10:49:25
 * @Copyright Copyright by 智多星
 * @Direction 类说明						JSAPI 支付方式传入的参数
 */

public class JSPayOrderRequestIn implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String successUrl ;						//支付成功后的回调	
	private String createIp ;						//支付发起方IP地址
	private Integer porOrderAmount;					//订单金额
	private String porGoodTitle;					//商品标题
	private String porGoodBody;						//商品描述
	private String porOutTradeNo;					//外部订单号
	private String openID ;							//用户openID	
	private String userid ;							//用户ID	
	
	public String getSuccessUrl() {
		return successUrl;
	}
	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}
	public String getCreateIp() {
		return createIp;
	}
	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}
	public Integer getPorOrderAmount() {
		return porOrderAmount;
	}
	public void setPorOrderAmount(Integer porOrderAmount) {
		this.porOrderAmount = porOrderAmount;
	}
	public java.lang.String getPorGoodTitle() {
		return porGoodTitle;
	}
	public void setPorGoodTitle(java.lang.String porGoodTitle) {
		this.porGoodTitle = porGoodTitle;
	}
	public java.lang.String getPorGoodBody() {
		return porGoodBody;
	}
	public void setPorGoodBody(java.lang.String porGoodBody) {
		this.porGoodBody = porGoodBody;
	}
	public java.lang.String getPorOutTradeNo() {
		return porOutTradeNo;
	}
	public void setPorOutTradeNo(java.lang.String porOutTradeNo) {
		this.porOutTradeNo = porOutTradeNo;
	}
	public String getOpenID() {
		return openID;
	}
	public void setOpenID(String openID) {
		this.openID = openID;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
	
	
	

}
