
package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
* @ClassName: HttpUtil
* @����: TODO
* @����:���߼���������÷��������ݿ��������ӣ���ѯ
* @���� zhangguang
* @date 2016-4-9 ����5:41:34
*
*/
public class HttpUtil {
	/**
	 * ������ݿ������һ�����߰�httputil
	 */
	
	public static void sendHttpResquest(final String address,final httpcallbacklistener listener){
		new Thread(new Runnable() {
		//����һ�������������߳�	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection=null;
				try {
					URL url=new URL(address);
					//��url��ַ������
					connection=(HttpURLConnection) url.openConnection();
					//���Ӳ�������
					connection.setRequestMethod("GET");
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					//����õ����������ݸ�io��ȡ
					InputStream in=connection.getInputStream();
					BufferedReader reader=new BufferedReader(new InputStreamReader(in));
					String line;
					StringBuilder response=new StringBuilder();
					while((line=reader.readLine())!=null){
						response.append(line);
					}
					//�����������Ӧ��ȡ�ɹ�����
					if(listener!=null){
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					// TODO: handle exception
					//�����������Ӧʧ�ܵ���
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
