
package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

/**
* @ClassName: Utility
* @描述: TODO
* @作用:用于解析服务器返回的数据库格式
* @作者 zhangguang
* @date 2016-4-9 下午6:03:21
*
*/
public class Utility {
	/**
	 * 解析服务器返回的省级数据
	 */
	public synchronized static boolean handleprovincesResponse(CoolWeatherDB coolWeatherdb,String response){
		if(!TextUtils.isEmpty(response)){
			String [] allprovince=response.split(",");
			if(allprovince!=null&&allprovince.length>0){
				for(String p:allprovince){
					Province province=new Province();
					String [] array=p.split("\\|");
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherdb.saveprovince(province);
				}
			}
			return true;
		}
		
		

		return false;	
	}
	
	
	/**
	 * 解析服务器返回的市级数据
	 */
	public static boolean handlecityresponse(final CoolWeatherDB coolWeatherDB,final String response,int provinceid){
		if(!TextUtils.isEmpty(response)){
			String [] allcity=response.split(",");
			if(allcity!=null&&allcity.length>0){
				for(String c:allcity){
					String [] array=c.split("\\|");
					City city =new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceid(provinceid);
					coolWeatherDB.savecity(city);
				}
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * 解析服务器返回的县级数据
	 */
	public static boolean handlecountyresponse(final CoolWeatherDB coolWeatherDB,final String response,int cityid){
		if(!TextUtils.isEmpty(response)){
			String [] allcounty=response.split(",");
			if(allcounty!=null&&allcounty.length>0){
				for(String c:allcounty){
					String [] p=c.split("\\|");
					County county=new County();
					county.setCountyCode(p[0]);
					county.setCountyName(p[1]);
					county.setCityid(cityid);
					coolWeatherDB.savecounty(county);
				}
			return true;
			}
		}
		return false;
	}
	
	/**
	 * 解析服务器返回的JSON数据，并将其解析出来
	 */
	public static void handleweatherResquest(Context context,String response){
		try {
			JSONObject jsonobject=new JSONObject(response);
			//获得json类型文件weatherinfo,从中间读取出信息,
			JSONObject weatherinfo=jsonobject.getJSONObject("weatherinfo");
			//利用键值对，根据json中的键返回值
			String cityname=weatherinfo.getString("city");
			String weathercode=weatherinfo.getString("cityid");
			String temp1=weatherinfo.getString("temp1");
			String temp2=weatherinfo.getString("temp2");
			String weatherDesp=weatherinfo.getString("weather");
			String publishTime=weatherinfo.getString("ptime");
			saveWeatherinfo(context,cityname,weathercode,temp1,temp2,weatherDesp,publishTime);
		} catch ( JSONException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


	private static void saveWeatherinfo(Context context, String cityname,
			String weathercode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		// TODO Auto-generated method stub
		//返回本地时间
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		
		editor.putBoolean("city_selectes", true);
		editor.putString("city_name", cityname);
		editor.putString("weather_code", weathercode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
}
