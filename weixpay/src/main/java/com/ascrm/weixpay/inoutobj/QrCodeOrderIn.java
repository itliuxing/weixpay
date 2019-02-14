package com.ascrm.weixpay.inoutobj;
/**
* @Class 	QrCodeOrder.java
* @Author 	作者姓名:刘兴 
* @Version	1.0
* @Date		创建时间：2018年9月28日 上午9:34:21
* @Copyright Copyright by 刘兴
* @Direction 类说明				二维码支付---》生成二维码需要传递的参数
*/
public class QrCodeOrderIn {
	
	private String orderid ;			// 订单号ID
	private String tradeName;			// 商品名称
	private Integer tradePrice;			// 商品价格 （单位：分）
	
	
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getTradeName() {
		return tradeName;
	}
	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}
	public Integer getTradePrice() {
		return tradePrice;
	}
	public void setTradePrice(Integer tradePrice) {
		this.tradePrice = tradePrice;
	}
	

	
}
