<%@ page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  language="java" %>

<!DOCTYPE html>
<%@include file="common/taglibs.jsp" %>
    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta http-equiv="Content-Language" content="zh-cn">
        <meta name="apple-mobile-web-app-capable" content="no"/>
        <meta name="apple-touch-fullscreen" content="yes"/>
        <meta name="format-detection" content="telephone=no,email=no"/>
        <meta name="apple-mobile-web-app-status-bar-style" content="white">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <title>支付详情 - 微信扫一扫支付</title>
        <link href="${ctx}/common/css/wechat_pay.css" rel="stylesheet" media="screen">
        <link rel="stylesheet" type="text/css" media="screen" href="${ctx}/common/css/font-awesome.min.css">
        <style>
            .text-success{
                color: #468847;
                font-size: 2.33333333em;
            }
            .text-center {
                text-align: center;
            }
        </style>
    </head>

    <body>
    <div class="body">
        <h1 class="mod-title">
            <span class="ico_log ico-3"></span>
        </h1>

        <div class="mod-ct">
            <div class="order">
            </div>
            <div class="amount" id="money" style="margin-left: -60px;" >￥10.00</div>
            <i style="width: 220px;height: 220px;zoom: 0.5;display: inline-block;
    			background: url(${ctx}/common/img/WeChatPay.png) no-repeat;"></i> 
            <h1 class="text-center text-success">
            <strong>
    			<span>支付成功</span>
    		</strong></h1>

            <div class="detail detail-open" id="orderDetail" style="display: block;">
                <dl class="detail-ct" id="desc">
                    <dt>金额</dt>
                    <dd>10.00</dd>
                    <dt>商户订单：</dt>
                    <dd>13187211621001001003</dd>
                    <dt>流水号：</dt>
                    <dd>13187211621001001003</dd>
                    <dt>付款时间：</dt>
                    <dd>2018-10-15 15:15:33</dd>
                    <dt>状态</dt>
                    <dd>支付成功</dd>
                </dl>


            </div>

            <div class="tip-text">
            </div>


        </div>
        <div class="foot">
            <div class="inner">
                
                <p>当您看到此信息表示已经通知了商户本次付款</p>
                <p>如果3秒内未到账您可联系商户的在线客服处理</p>
                
            </div>
        </div>

    </div>
    <div class="copyRight">
        <p>支付合作：<a href="http://codepay.fateqq.com/" target="_blank">码支付</a></p>
    </div>

    </body>
    </html>
