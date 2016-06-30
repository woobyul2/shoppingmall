package com.binrui.shop.express.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.binrui.shop.utils.ApiUtils;

/**
 * ��ѯ����������
 * @author lijinfeng
 * @version 1.0
 * @date 2015-08-02 21:11
 * @title <p>��ѯ����������</p>
 */
@Service("expressService")
public class ExpressService {
	/**
	 * ��ѯ�����������
	 * @param key 		�����Ȩkey���� kuaidi100 �������루��Сд���У�
	 * @param com		Ҫ��ѯ�Ŀ�ݹ�˾���룬��֧�����ģ���Ӧ�Ĺ�˾����[�ο����100�ĵ�]
	 * @param number	��ݵ�����
	 * @return
	 * {"message":"ok","status":"1","state":"3","data":
     *       [{"time":"2012-07-07 13:35:14","context":"�ͻ���ǩ��"},
     *        {"time":"2012-07-07 09:10:10","context":"�뿪 [����ʯ��ɽӪҵ��] �����У�����Ա[��]���绰[]"},
     *        {"time":"2012-07-06 19:46:38","context":"���� [����ʯ��ɽӪҵ��]"},
     *        {"time":"2012-07-06 15:22:32","context":"�뿪 [����ʯ��ɽӪҵ��] �����У�����Ա[��]���绰[]"},
     *        {"time":"2012-07-06 15:05:00","context":"���� [����ʯ��ɽӪҵ��]"},
     *        {"time":"2012-07-06 13:37:52","context":"�뿪 [����_ͬ����תվ] ���� [����ʯ��ɽӪҵ��]"},
     *        {"time":"2012-07-06 12:54:41","context":"���� [����_ͬ����תվ]"},
     *        {"time":"2012-07-06 11:11:03","context":"�뿪 [������ת����פվ����] ���� [����_ͬ����תվ]"},
     *        {"time":"2012-07-06 10:43:21","context":"���� [������ת����פվ����]"},
     *        {"time":"2012-07-05 21:18:53","context":"�뿪 [����_����֧��˾] ���� [������ת����_����]"},
     *        {"time":"2012-07-05 20:07:27","context":"��ȡ�������� [����_����֧��˾]"}
     *  ]} 
	 */
	@SuppressWarnings("static-access")
	public JSONObject queryOrderDelivery(String key,String com,String number){
		String param = "?id="+key+"&com="+com+"&nu="+number+"&show=0&muti=0&order=desc";
		try{
			URL url= new URL(ApiUtils.KUAIDI100_API_URL+param);
			URLConnection con=url.openConnection();
			con.setAllowUserInteraction(false);
			InputStream urlStream = url.openStream();
			String type = con.guessContentTypeFromStream(urlStream);
		    String charSet=null;
			if (type == null)
				type = con.getContentType();
			if (type == null || type.trim().length() == 0 || type.trim().indexOf("text/html") < 0)
				return null;

			if(type.indexOf("charset=") > 0)
			    charSet = type.substring(type.indexOf("charset=") + 8);

			byte b[] = new byte[10000];
			int numRead = urlStream.read(b);
			String content = new String(b, 0, numRead);
			while (numRead != -1) {
			numRead = urlStream.read(b);
			if (numRead != -1) {
				//String newContent = new String(b, 0, numRead);
				String newContent = new String(b, 0, numRead, charSet);
				content += newContent;
				}
			}
			//System.out.println("content:" + content);
			urlStream.close();
			if(StringUtils.isNotBlank(content)){
				JSONObject json = JSONObject.parseObject(content);
				return json;
			}
		} catch (MalformedURLException e){
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * kuaidi100 API��ݹ�˾����
	 * @author lijinfeng
	 * {
	 * ���Ŵ�  anxindakuaixi,
	 * ������ͨ huitongkuaidi,
	 * ����ƽ��/�Һ��� youzhengguonei,
	 * �������� bangsongwuliu
	 * �°����� debangwuliu
	 * EMS��� ems
	 * ��ͨ��� guotongkuaidi
	 * ��ͨ��� huitongkuaidi
	 * ��ͨ��� shentong
	 * ˳���� shunfeng
	 * 2015-06-10
	 */
	public static enum ExpressCode { 
		AXD("anxindakuaixi"),BSHT("huitongkuaidi"),BGPY("youzhengguonei"),
		DBWL("debangwuliu"),EMS("ems"),GTKD("guotongkuaidi"),HTKD("huitongkuaidi"),
		STKD("shentong"),SFKD("shunfeng");
		private String code;
		ExpressCode(String code){
			this.code = code;
		}
		public String getCode() { 
			return this.code; 
		} 
	}
}
