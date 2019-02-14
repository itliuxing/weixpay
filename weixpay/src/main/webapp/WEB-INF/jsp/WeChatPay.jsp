<%@ page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"  language="java" %>

<!DOCTYPE html>
<%@include file="common/taglibs.jsp" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8">
    <meta http-equiv="Content-Language" content="zh-cn">
    <meta name="apple-mobile-web-app-capable" content="no"/>
    <meta name="apple-touch-fullscreen" content="yes"/>
    <meta name="format-detection" content="telephone=no,email=no"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="white">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <title>微信扫码支付</title>
    <link href="${ctx}/common/css/wechat_pay.css" rel="stylesheet" media="screen">

</head>

<body>
<div class="body">
    <h1 class="mod-title">
        <span class="ico_log ico-3"></span>
    </h1>

    <div class="mod-ct">
        <div class="order">
        </div>
        <div class="amount" id="money">￥10</div>
        <div class="qrcode-img-wrapper" data-role="qrPayImgWrapper">
            <div data-role="qrPayImg" class="qrcode-img-area">
                <div class="ui-loading qrcode-loading" data-role="qrPayImgLoading" style="display: none;">加载中</div>
                <div style="position: relative;display: inline-block;">
                    <img id='show_qrcode' alt="加载中..." src="${ctx}/common/img/no.png" width="210" height="210" style="display: block;">
                    <img onclick="$('#use').hide()" id="use"
                         src="${ctx}/common/img/use_3.png"
                         style="position: absolute;top: 50%;left: 50%;width:32px;height:32px;margin-left: -21px;margin-top: -21px">
                </div>
            </div>


        </div>
        <!-- 这里可以输入你想要的提示!-->
        <div class="time-item" id="msg">
			<h1 id="orderdesc">二维码过期倒计时</h1>
            <strong id="hour_show">0时</strong>
            <strong id="minute_show">0分</strong>
            <strong id="second_show">0秒</strong>
        </div>

        <div class="tip">
            <div class="ico-scan"></div>
            <div class="tip-text">
                <p>请使用微信扫一扫</p>
                <p>扫描二维码完成支付</p>
            </div>
        </div>

        <div class="detail" id="orderDetail">
            <dl class="detail-ct" id="desc" style="display: none;">

                <dt>状态</dt>
                <dd id="createTime">订单创建</dd>

            </dl>
            <a href="javascript:void(0)" class="arrow"><i class="ico-arrow"></i></a>
        </div>

        <div class="tip-text">
        </div>


    </div>
    <div class="foot">
        <div class="inner">
            <p>手机用户扫一扫上方二维码支付即可</p>
        </div>
    </div>

</div>


<!--注意下面加载顺序 顺序错乱会影响业务-->
<script src="${ctx}/common/js/jquery-1.10.2.min.js"></script>
<!--[if lt IE 8]>
<script src="http://ossali.sucaihuo.com/sucaihuo/Public/mianqian/js/json3.min.js"></script><![endif]-->

<script src="${ctx}/common/js/codepay_util.js"></script>
<script>
	callback(
		{"msg":"微信支付需要商家登录支付后台重新授权",
			"qrcode":"${ctx}/weichat_p/qr",		//二维码支付路径
			"chart":"utf-8",		//编码
			"price":"10.00",		//订单金额
			"money":"10.00",		//支付金额
			"userID":16403,			//用户ID
			"notiry_host":"${ctx}/weichat_p/query?trade_no=13187211621001001003",	  //查询接口
			"success_host":"${ctx}/weichat_p/paysucc?trade_no=13187211621001001003",//支付成功后，跳转至....
			"trade_no":13187211621001001003,	//订单号
			"outTime":600			//定制超时(s)
		}
	);
</script>
<script>
    setTimeout(function () {
        $('#use').hide() //2秒后隐藏中间那LOGO
    }, 2000);
</script>
</body>
</html>