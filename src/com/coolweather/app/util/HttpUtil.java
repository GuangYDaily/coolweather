
package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
* @ClassName: HttpUtil
* @描述: TODO
* @作用:工具集，用来获得服务器数据库数据连接，查询
* @作者 zhangguang
* @date 2016-4-9 下午5:41:34
*
*/
public class HttpUtil {
	/**
	 * 获得数据库请求的一个工具包httputil
	 */
	
	public static void sendHttpResquest(final String address,final httpcallbacklistener listener){
		new Thread(new Runnable() {
		//开启一个服务器请求线程	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection=null;
				try {
					URL url=new URL(address);
					//打开url地址的连接
					connection=(HttpURLConnection) url.openConnection();
					//连接参数设置
					connection.setRequestMethod("GET");
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					//将获得的数据流传递给io读取
					InputStream in=connection.getInputStream();
					BufferedReader reader=new BufferedReader(new InputStreamReader(in));
					String line;
					StringBuilder response=new StringBuilder();
					while((line=reader.readLine())!=null){
						response.append(line);
					}
					//如果服务器响应读取成功调用
					if(listener!=null){
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					// TODO: handle exception
					//如果服务器响应失败调用
					if(listener!=null){
						listener.onError(e);
					}
				}finally{
					if(connection!=null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
