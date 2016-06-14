
package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
* @ClassName: CoolWeatherDB
* @描述: TODO
* @作用:常用的数据库操作方法进行封装
* @作者 zhangguang
* @date 2016-4-8 下午9:29:56
*
*/
public class CoolWeatherDB {
	
	/**
	 * 数据库名称和版本
	 * @param context
	 */
	public static final String DB_NAME="cool_weather.db";
	public static final int VERSION=1;
	
	private static SQLiteDatabase db;
	private static CoolWeatherDB coolWeatherDB;
	
	/**
	 * 构造方法私有化，用于对coolweatheropenhelper传入参数
	 * @param context
	 */ 
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbhelper=new CoolWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db=dbhelper.getWritableDatabase();
	}
	
	/**
	 * 获得coollweatheropenhelperde全局唯一调用方法
	 * 以后想调用coolweatherdb只有调用getinstance，返回值为CoolWeatherDB
	 * @param context
	 */
	public synchronized static CoolWeatherDB getinstance(Context context){
		if(coolWeatherDB==null){
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/**
	 *将province省级数据插入到数据库到数据库 
	 */
	public void saveprovince(Province province){
		if(province!=null){
			//contentvalues这个类是用来存储一组的ContentResolver的可以处理的值
			ContentValues contentvalues=new ContentValues();
			contentvalues.put("province_name", province.getProvinceName());
			contentvalues.put("province_code", province.getProvinceCode());
			db.insert("province", null, contentvalues);
		}
	}
	
	/**
	 * 将全国的省份数据从数据库里面查询出来
	 */
	public List<Province> loadprovince(){
		/**cursor该接口提供对由数据库查询返回的结果集的随机读写访问。不需要光标实现能同步，
		*以便使用从多个线程光标应该履行自己的同步使用游标时的代码。
		*实现应该继承AbstractCursor*/
		List<Province> listprovince=new ArrayList<Province>();
		
		Cursor cursor=db.query("province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
			//将数据封装到province对象里面，以list为返回查询结果集
				Province province=new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				listprovince.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return listprovince;
	}
	
	/**
	 * 将city实例存储到数据库
	 * 
	 */
	public void savecity(City city){
		
		if(city!=null){
			ContentValues contentValues=new ContentValues();
			contentValues.put("city_name", city.getCityName());
			contentValues.put("city_code", city.getCityCode());
			contentValues.put("province_id", city.getProvinceid());
			db.insert("city", null, contentValues);
		}
	}
	
	/**
	 * 在某省下面对市级数据的查询
	 */
	public List<City> loadcity(int provinceid){
		List<City> listcity=new ArrayList<City>();
		//在市级数据库里，判断是某一身份的数据
		Cursor cursor=db.query("city", null,"province_id=?",new String[]{String.valueOf(provinceid)},null,null,null);
		if(cursor.moveToFirst()){
			do{City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceid(cursor.getInt(cursor.getColumnIndex("province_id")));
				listcity.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return listcity;
	}

	/**
	 * 将county实例化
	 */
	public void savecounty(County county){
		ContentValues contentvalues=new ContentValues();
		if(contentvalues!=null){
			contentvalues.put("county_name", county.getCountyName());
			contentvalues.put("county_code", county.getCountyCode());
			contentvalues.put("city_id", county.getCityid());
			db.insert("county", null, contentvalues);
		}
	}

	/**
	 * 从数据库读取市级下的所有县城
	 */
	public List<County> loadcounty(int cityid){
		List<County> listcounty=new ArrayList<County>();
		Cursor cursor=db.query("county", null, "city_id=?", new String[]{String.valueOf(cityid)}, null, null,null);
		if(cursor.moveToFirst()){
			do{
				County county=new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityid(cursor.getInt(cursor.getColumnIndex("city_id")));
				listcounty.add(county);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return listcounty;
	}
}
