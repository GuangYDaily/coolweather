
package com.coolweather.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
* @ClassName: CoolWeatherOpenHelper
* @����: CoolWeather
* @����: �������ݿ��ʼ��
* @���� zhangguang
* @date 2016-4-8 ����8:54:22
*
*/
public class CoolWeatherOpenHelper extends SQLiteOpenHelper {
	/**
	 * province�������
	 */
	public static final String CREATE_PROVINCE="create table province(" +
			"id integer primary key autoincrement," +
			"province_name text," +
			"province_code text)";
	/**
	 * city��Ĵ���
	 */
	public static final String CREATE_CITY="create table city(" +
			"id integer primary key autoincrement," +
			"city_name text," +
			"city_code text," +
			"province_id integer)";
	
	/**
	 * county��Ĵ���
	 */
	public static final String CREATE_COUNTY="create table county(" +
			"id integer primary key autoincrement," +
			"county_name text," +
			"county_code text," +
			"city_id integer)";
	
	
	/**
	 * 
	 * ���췽�����ڵ���
	 */
	public CoolWeatherOpenHelper(Context context, String name,
			
			CursorFactory factory, int version) {
		
		//��������ǻ��������ݿ��ļ����α����Ϊnull���汾��
		super(context, name, factory, version);
		
	}
	/**
	 * �������ݿ�
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);//����ʡ�����ݿ�
		db.execSQL(CREATE_CITY);	//�����е����ݿ�
		db.execSQL(CREATE_COUNTY);	//�����ص����ݿ�
	}
	/**
	 * �������ݿ�
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
