package com.ascrm.weixpay.inoutobj;

import java.io.Serializable;

/**
 * @Class 	PayAccountRequest.java
 * @Author 	作者姓名:刘兴 
 * @Version	1.0
 * @Date	创建时间：2017-10-13 上午10:49:25
 * @Copyright Copyright by 智多星
 * @Direction 类说明
 */

public class JSPayOrderRequestOut implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String appId ;
	private Long timeStamp ;
	private String nonceStr ;
	private String prepayId ;
	private String signType ;
	private String paySign ;
	

	private String payOrderID ;
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getPrepayId() {
		return prepayId;
	}
	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}
	
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getPaySign() {
		return paySign;
	}
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}
	public String getPayOrderID() {
		return payOrderID;
	}
	public void setPayOrderID(String payOrderID) {
		this.payOrderID = payOrderID;
	}

	
	
	

}
