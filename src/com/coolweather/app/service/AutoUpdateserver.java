
package com.coolweather.app.service;

import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import com.coolweather.app.util.httpcallbacklistener;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;


/**
* @ClassName: AutoUpdateserver
* @描述: TODO
* @作用:这是一个服务，用来后台自动更新天气
* @作者 zhangguang
* @date 2016-4-12 下午3:19:06
*
*/
public class AutoUpdateserver extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateweather();
			}
		}).start();
		//设置更新时间
		AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
		int autor=24*60*60*1000;
		long triggertime=SystemClock.elapsedRealtime()+autor;
		//定时事件
		Intent intents=new Intent(this,AutoUpdateserver.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, intents, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggertime, pi);
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
	private void updateweather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		String weathercode=prefs.getString("weather_code", "");
		String address="http://www.weather.com.cn/data/cityinfo/"+weathercode+".html";
		HttpUtil.sendHttpResquest(address, new httpcallbacklistener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Utility.handleweatherResquest(AutoUpdateserver.this, response);
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		});
	}
}
