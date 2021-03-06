package com.distribution.wxpay.demo;

import com.distribution.common.utils.MoneyUtil;
import com.distribution.wxpay.utils.GetWxOrderno;
import com.distribution.wxpay.utils.RequestHandler;
import com.distribution.wxpay.utils.Sha1Util;
import com.distribution.wxpay.utils.TenpayUtil;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * @author ex_yangxiaoyi
 * 
 */
public class Demo {
	//微信支付商户开通后 微信会提供appid和appsecret和商户号partner
	private static String appid = "wx2ec7549343d60f3a";
	private static String appsecret = null;
	private static String partner = "1232840302";
	//这个参数partnerkey是在商户后台配置的一个32位的key,微信商户平台-账户设置-安全设置-api安全
	private static String partnerkey = "760569611f4746a4b1b107e46317mlgb";
	//openId 是微信用户针对公众号的标识，授权的部分这里不解释
//	private static String openId = "";
	//微信支付成功后通知地址 必须要求80端口并且地址不能带参数
	private static String notifyurl = "http://www.weixin.qq.com/wxpay/pay.php";

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		
//		//微信支付jsApi
//		WxPayDto tpWxPay = new WxPayDto();
//		tpWxPay.setOpenId(openId);
//		tpWxPay.setBody("商品信息");
//		tpWxPay.setOrderId(getNonceStr());
//		tpWxPay.setSpbillCreateIp("127.0.0.1");
//		tpWxPay.setTotalFee("0.01");
//	    getPackage(tpWxPay);
		InetAddress ia= InetAddress.getLocalHost();
		
		String localname=ia.getHostName();
		String localip=ia.getHostAddress();
		System.out.println("本机名称是："+ localname);
		System.out.println("本机的ip是 ："+localip);
		
	    
	    //扫码支付
	    WxPayDto tpWxPay1 = new WxPayDto();
	    tpWxPay1.setBody("Ipad mini  16G  白色");
	    tpWxPay1.setOrderId("0150806125346");
	    tpWxPay1.setSpbillCreateIp(localip);
	    tpWxPay1.setTotalFee("0.01");
		getCodeurl(tpWxPay1);

	}
	
	/**
	 * 获取微信扫码支付二维码连接
	 */
	public static String getCodeurl(WxPayDto tpWxPayDto){
		
		// 1 参数
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		// 附加数据 原样返回
//		String attach = "";
		// 总金额以分为单位，不带小数点
		String totalFee = MoneyUtil.convertMoney2Cent(tpWxPayDto.getTotalFee()).toString();
		
		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = notifyurl;
		String trade_type = "NATIVE";

		// 商户号
		String mch_id = partner;
		// 随机字符串
		String nonce_str = getNonceStr();
//		String nonce_str = "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";

		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();

		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("body", body);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("notify_url", notify_url);
//		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("spbill_create_ip", spbill_create_ip);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("trade_type", trade_type);
//		packageParams.put("product_id", "123123");

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		System.out.println("sign:"+sign);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "</xml>";
		String code_url = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		System.out.println("xml:"+xml);
		code_url = GetWxOrderno.getCodeUrl(createOrderURL, xml);
		System.out.println("code_url----------------"+code_url);
		
		return code_url;
	}
	
	
	/**
	 * 获取请求预支付id报文
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static String getPackage(WxPayDto tpWxPayDto) {
		
		String openId = tpWxPayDto.getOpenId();
		// 1 参数
		// 订单号
		String orderId = tpWxPayDto.getOrderId();
		// 附加数据 原样返回
		String attach = "";
		// 总金额以分为单位，不带小数点
		String totalFee = MoneyUtil.convertMoney2Cent(tpWxPayDto.getTotalFee()).toString();
		
		// 订单生成的机器 IP
		String spbill_create_ip = tpWxPayDto.getSpbillCreateIp();
		// 这里notify_url是 支付完成后微信发给该链接信息，可以判断会员是否支付成功，改变订单状态等。
		String notify_url = notifyurl;
		String trade_type = "JSAPI";

		// ---必须参数
		// 商户号
		String mch_id = partner;
		// 随机字符串
		String nonce_str = getNonceStr();

		// 商品描述根据情况修改
		String body = tpWxPayDto.getBody();

		// 商户订单号
		String out_trade_no = orderId;

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("body", body);
		packageParams.put("attach", attach);
		packageParams.put("out_trade_no", out_trade_no);

		// 这里写的金额为1 分到时修改
		packageParams.put("total_fee", totalFee);
		packageParams.put("spbill_create_ip", spbill_create_ip);
		packageParams.put("notify_url", notify_url);

		packageParams.put("trade_type", trade_type);
		packageParams.put("openid", openId);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, appsecret, partnerkey);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>"
				+ mch_id + "</mch_id>" + "<nonce_str>" + nonce_str
				+ "</nonce_str>" + "<sign>" + sign + "</sign>"
				+ "<body><![CDATA[" + body + "]]></body>" 
				+ "<out_trade_no>" + out_trade_no
				+ "</out_trade_no>" + "<attach>" + attach + "</attach>"
				+ "<total_fee>" + totalFee + "</total_fee>"
				+ "<spbill_create_ip>" + spbill_create_ip
				+ "</spbill_create_ip>" + "<notify_url>" + notify_url
				+ "</notify_url>" + "<trade_type>" + trade_type
				+ "</trade_type>" + "<openid>" + openId + "</openid>"
				+ "</xml>";
		String prepay_id = "";
		String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
		
		
		prepay_id = GetWxOrderno.getPayNo(createOrderURL, xml);

		System.out.println("获取到的预支付ID：" + prepay_id);
		
		
		//获取prepay_id后，拼接最后请求支付所需要的package
		
		SortedMap<String, String> finalpackage = new TreeMap<String, String>();
		String timestamp = Sha1Util.getTimeStamp();
		String packages = "prepay_id="+prepay_id;
		finalpackage.put("appId", appid);  
		finalpackage.put("timeStamp", timestamp);  
		finalpackage.put("nonceStr", nonce_str);  
		finalpackage.put("package", packages);  
		finalpackage.put("signType", "MD5");
		//要签名
		String finalsign = reqHandler.createSign(finalpackage);
		
		String finaPackage = "\"appId\":\"" + appid + "\",\"timeStamp\":\"" + timestamp
		+ "\",\"nonceStr\":\"" + nonce_str + "\",\"package\":\""
		+ packages + "\",\"signType\" : \"MD5" + "\",\"paySign\":\""
		+ finalsign + "\"";

		System.out.println("V3 jsApi package:"+finaPackage);
		return finaPackage;
	}

	/**
	 * 获取随机字符串
	 * @return
	 */
	public static String getNonceStr() {
		// 随机数
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		return strTime + strRandom;
	}



}
