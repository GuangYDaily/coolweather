
package com.coolweather.app.activity;


import com.com.coolweather.app.R;
import com.coolweather.app.service.AutoUpdateserver;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import com.coolweather.app.util.httpcallbacklistener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
* @ClassName: WeatherActivity
* @����: TODO
* @����:������ʾ�����Ļ��
* @���� zhangguang
* @date 2016-4-11 ����3:16:20
*
*/

public class WeatherActivity extends BaseActivity implements OnClickListener {
	/**
	 * ������
	 */
	ProgressDialog progressDialog;
	/**
	 * ѡ�����
	 */
	private Button homecity;
	
	/**
	 * �ֶ�ˢ��
	 */
	private Button refresh;
	
	/**
	 * ���ز��ֽ�ȥ
	 */
	private LinearLayout linearlayout;
	/**
	 * ������ʾ������
	 */
	private TextView citynametText;
	/**
	 * ������ʾ����ʱ��
	 */
	private TextView publishtext;
	/**
	 * ������ʾ����������Ϣ
	 */
	private TextView weathdespText;
	/**
	 * ������ʾ�¶�temp1
	 */
	private TextView temp1;
	/**
	 * ������ʾtemp2
	 */
	private TextView temp2;
	/**
	 * ������ʾ��ǰ����
	 */
	private TextView currentdatetext;
	
	
	/**
	 * �㲥�¼�
	 */
	
	private IntentFilter intenfiltert;
	private NotworkBroadcastRecivier netbroad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//��ʼ�����ؼ�
		linearlayout=(LinearLayout) findViewById(R.id.weather_info_layout);
		citynametText=(TextView) findViewById(R.id.city_name);
		publishtext=(TextView) findViewById(R.id.publish_text);
		weathdespText=(TextView) findViewById(R.id.weather_desp);
		temp1=(TextView) findViewById(R.id.temp1_id);
		temp2=(TextView) findViewById(R.id.temp2_id);
		currentdatetext=(TextView) findViewById(R.id.current_date);
		//�����������ֶ�����
		homecity=(Button)findViewById(R.id.home_id);
		refresh=(Button)findViewById(R.id.refresh_weatherr);
		//��ť�¼�
		homecity.setOnClickListener(this);
		refresh.setOnClickListener(this);
		//��ȡchooseactivity����ݵ�ֵ
		String countycode=getIntent().getStringExtra("county_code");
		
		
		//�����������Ĺ㲥
		intenfiltert=new IntentFilter();
		intenfiltert.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		netbroad=new NotworkBroadcastRecivier();
		
		//���������ķ���
		Intent intentq=new Intent(this,AutoUpdateserver.class);
		startService(intentq);
		
		
		//ȥ��ѯ����
		if(!TextUtils.isEmpty(countycode)){
			linearlayout.setVisibility(View.INVISIBLE);
			citynametText.setVisibility(View.INVISIBLE);
			//���ؼ����ž�ȥ��ѯ����
			queryweathercode(countycode);
		}else{
			//û���ؼ����ž�ȥ��ʾ��������
			showweather();
		}
		
		
	}
	
	/**
	 * ����ˢ�º��л������¼�
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.home_id:
					Intent intent=new Intent(this,ChooseAreaActivity.class);
					intent.putExtra("from_weather_city", true);
					startActivity(intent);
					finish();
					break;
			
		case R.id.refresh_weatherr:
			Toast.makeText(this, "ˢ����", Toast.LENGTH_LONG).show();
			SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(this);
			String weathercode=pref.getString("weather_code", "");
			if(!TextUtils.isEmpty(weathercode)){
				queryweatherinfo(weathercode);
			}
		}
	}
	
	
	
	/**
	 * ��ѯ�ؼ����Ŷ�Ӧ����������
	 * @param countycode
	 */
	private void queryweathercode(String countycode) {
		// TODO Auto-generated method stub
		String address="http://www.weather.com.cn/data/list3/city"+countycode+".xml";
		queryFromserver(address,"countycode");
	}
	
	
	/**
	 * ��ѯ��������˵��Ӧ������
	 */
	private void queryweatherinfo(String weathercode){
		registerReceiver(netbroad, intenfiltert);
		String address="http://www.weather.com.cn/data/cityinfo/"+weathercode+".html";
		queryFromserver(address, "weathercode");
	}
	
	/**
	 * ���ݴ�������ͺ͵�ַȥ��������ѯ
	 * @param address
	 * @param type
	 */
	private void queryFromserver(final String address, final String type) {
		// TODO Auto-generated method stub
		HttpUtil.sendHttpResquest(address, new httpcallbacklistener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				//��������������ֱ����ʾ�����û�У���ʼ��ѯ
				if("countycode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						String [] array=response.split("\\|");
						if(array!=null&&array.length==2){
							String weathercode=array[1];
							queryweatherinfo(weathercode);
						}
					}
				}else if("weathercode".equals(type)){
					Utility.handleweatherResquest(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						public void run() {
							showweather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				new Runnable() {
					public void run() {
						publishtext.setText("ͬ��ʧ��");
					}
				};
			}
		});
	}
	
	/**
	 * ������ʾ��������
	 */
	private void showweather() {
		// TODO Auto-generated method stub
		ProgressDialog pro=netbroad.progress();
		
		if(pro!=null){
			pro.dismiss();
		}
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		citynametText.setText(prefs.getString("city_name", ""));
		temp1.setText(prefs.getString("temp1", ""));
		temp2.setText(prefs.getString("temp2", ""));
		weathdespText.setText(prefs.getString("weather_desp", ""));
		publishtext.setText("����"+prefs.getString("publish_time", "")+"����");
		currentdatetext.setText(prefs.getString("current_date", ""));
		linearlayout.setVisibility(View.VISIBLE);
		citynametText.setVisibility(View.VISIBLE);
	}
}








class NotworkBroadcastRecivier extends BroadcastReceiver{
	 private ProgressDialog progressdialog;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ConnectivityManager connectivitymanger=(ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo networkinfo=connectivitymanger.getActiveNetworkInfo();
		if(networkinfo!=null&&networkinfo.isAvailable()){
			progressdialog=new ProgressDialog(context);
			progressdialog.setMessage("����ͬ��");
			progressdialog.setCancelable(false);
			progressdialog.show();
			
		}else{
			progressdialog=new ProgressDialog(context);
			progressdialog.setMessage("û������,ˢ�����԰�");
			progressdialog.setCancelable(true);
			progressdialog.show();
		}
	}
	public ProgressDialog progress(){
		return progressdialog;
	}

}
