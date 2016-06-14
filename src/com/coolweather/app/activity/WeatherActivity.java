
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
* @描述: TODO
* @作用:用于显示天气的活动类
* @作者 zhangguang
* @date 2016-4-11 下午3:16:20
*
*/

public class WeatherActivity extends BaseActivity implements OnClickListener {
	/**
	 * 进度条
	 */
	ProgressDialog progressDialog;
	/**
	 * 选择城市
	 */
	private Button homecity;
	
	/**
	 * 手动刷新
	 */
	private Button refresh;
	
	/**
	 * 加载布局进去
	 */
	private LinearLayout linearlayout;
	/**
	 * 用于显示城市名
	 */
	private TextView citynametText;
	/**
	 * 用于显示发布时间
	 */
	private TextView publishtext;
	/**
	 * 用于显示天气描述信息
	 */
	private TextView weathdespText;
	/**
	 * 用于显示温度temp1
	 */
	private TextView temp1;
	/**
	 * 用于显示temp2
	 */
	private TextView temp2;
	/**
	 * 用于显示当前日期
	 */
	private TextView currentdatetext;
	
	
	/**
	 * 广播事件
	 */
	
	private IntentFilter intenfiltert;
	private NotworkBroadcastRecivier netbroad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		//初始化各控件
		linearlayout=(LinearLayout) findViewById(R.id.weather_info_layout);
		citynametText=(TextView) findViewById(R.id.city_name);
		publishtext=(TextView) findViewById(R.id.publish_text);
		weathdespText=(TextView) findViewById(R.id.weather_desp);
		temp1=(TextView) findViewById(R.id.temp1_id);
		temp2=(TextView) findViewById(R.id.temp2_id);
		currentdatetext=(TextView) findViewById(R.id.current_date);
		//更新天气与手动更新
		homecity=(Button)findViewById(R.id.home_id);
		refresh=(Button)findViewById(R.id.refresh_weatherr);
		//按钮事件
		homecity.setOnClickListener(this);
		refresh.setOnClickListener(this);
		//获取chooseactivity活动传递的值
		String countycode=getIntent().getStringExtra("county_code");
		
		
		//获得有无网络的广播
		intenfiltert=new IntentFilter();
		intenfiltert.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		netbroad=new NotworkBroadcastRecivier();
		
		//更新天气的服务
		Intent intentq=new Intent(this,AutoUpdateserver.class);
		startService(intentq);
		
		
		//去查询天气
		if(!TextUtils.isEmpty(countycode)){
			linearlayout.setVisibility(View.INVISIBLE);
			citynametText.setVisibility(View.INVISIBLE);
			//有县级代号就去查询天气
			queryweathercode(countycode);
		}else{
			//没有县级代号就去显示本地天气
			showweather();
		}
		
		
	}
	
	/**
	 * 触发刷新和切换城市事件
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
			Toast.makeText(this, "刷新中", Toast.LENGTH_LONG).show();
			SharedPreferences pref=PreferenceManager.getDefaultSharedPreferences(this);
			String weathercode=pref.getString("weather_code", "");
			if(!TextUtils.isEmpty(weathercode)){
				queryweatherinfo(weathercode);
			}
		}
	}
	
	
	
	/**
	 * 查询县级代号对应的天气代号
	 * @param countycode
	 */
	private void queryweathercode(String countycode) {
		// TODO Auto-generated method stub
		String address="http://www.weather.com.cn/data/list3/city"+countycode+".xml";
		queryFromserver(address,"countycode");
	}
	
	
	/**
	 * 查询天气代号说对应的天气
	 */
	private void queryweatherinfo(String weathercode){
		registerReceiver(netbroad, intenfiltert);
		String address="http://www.weather.com.cn/data/cityinfo/"+weathercode+".html";
		queryFromserver(address, "weathercode");
	}
	
	/**
	 * 根据传入的类型和地址去服务器查询
	 * @param address
	 * @param type
	 */
	private void queryFromserver(final String address, final String type) {
		// TODO Auto-generated method stub
		HttpUtil.sendHttpResquest(address, new httpcallbacklistener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				//如果有天气代码就直接显示，如果没有，开始查询
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
						publishtext.setText("同步失败");
					}
				};
			}
		});
	}
	
	/**
	 * 用于显示天气界面
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
		publishtext.setText("今天"+prefs.getString("publish_time", "")+"发布");
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
			progressdialog.setMessage("正在同步");
			progressdialog.setCancelable(false);
			progressdialog.show();
			
		}else{
			progressdialog=new ProgressDialog(context);
			progressdialog.setMessage("没有网络,刷新试试吧");
			progressdialog.setCancelable(true);
			progressdialog.show();
		}
	}
	public ProgressDialog progress(){
		return progressdialog;
	}

}
