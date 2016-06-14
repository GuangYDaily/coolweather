
package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
* @ClassName: CoolWeatherOpenHelper
* @描述: CoolWeather
* @作用: 创建数据库初始化
* @作者 zhangguang
* @date 2016-4-8 下午8:54:22
*
*/
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	/**
	 * province表创建语句
	 */
	public static final String CREATE_PROVINCE="create table province(" +
			"id integer primary key autoincrement," +
			"province_name text," +
			"province_code text)";
	/**
	 * city表的创建
	 */
	public static final String CREATE_CITY="create table city(" +
			"id integer primary key autoincrement," +
			"city_name text," +
			"city_code text," +
			"province_id integer)";
	
	/**
	 * county表的创建
	 */
	public static final String CREATE_COUNTY="create table county(" +
			"id integer primary key autoincrement," +
			"county_name text," +
			"county_code text," +
			"city_id integer)";
	
	
	/**
	 * 
	 * 构造方法用于调用
	 */
	public CoolWeatherOpenHelper(Context context, String name,
			
			CursorFactory factory, int version) {
		
		//传入参数是环境，数据库文件，游标可以为null，版本号
		super(context, name, factory, version);
		
	}
	/**
	 * 创建数据库
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);//创建省的数据库
		db.execSQL(CREATE_CITY);	//创建市的数据库
		db.execSQL(CREATE_COUNTY);	//创建县的数据库
	}
	/**
	 * 更新数据库
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
