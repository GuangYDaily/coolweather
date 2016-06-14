
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
* @����: TODO
* @����:���õ����ݿ�����������з�װ
* @���� zhangguang
* @date 2016-4-8 ����9:29:56
*
*/
public class CoolWeatherDB {
	
	/**
	 * ���ݿ����ƺͰ汾
	 * @param context
	 */
	public static final String DB_NAME="cool_weather.db";
	public static final int VERSION=1;
	
	private static SQLiteDatabase db;
	private static CoolWeatherDB coolWeatherDB;
	
	/**
	 * ���췽��˽�л������ڶ�coolweatheropenhelper�������
	 * @param context
	 */ 
	private CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbhelper=new CoolWeatherOpenHelper(context,
				DB_NAME, null, VERSION);
		db=dbhelper.getWritableDatabase();
	}
	
	/**
	 * ���coollweatheropenhelperdeȫ��Ψһ���÷���
	 * �Ժ������coolweatherdbֻ�е���getinstance������ֵΪCoolWeatherDB
	 * @param context
	 */
	public synchronized static CoolWeatherDB getinstance(Context context){
		if(coolWeatherDB==null){
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	/**
	 *��provinceʡ�����ݲ��뵽���ݿ⵽���ݿ� 
	 */
	public void saveprovince(Province province){
		if(province!=null){
			//contentvalues������������洢һ���ContentResolver�Ŀ��Դ����ֵ
			ContentValues contentvalues=new ContentValues();
			contentvalues.put("province_name", province.getProvinceName());
			contentvalues.put("province_code", province.getProvinceCode());
			db.insert("province", null, contentvalues);
		}
	}
	
	/**
	 * ��ȫ����ʡ�����ݴ����ݿ������ѯ����
	 */
	public List<Province> loadprovince(){
		/**cursor�ýӿ��ṩ�������ݿ��ѯ���صĽ�����������д���ʡ�����Ҫ���ʵ����ͬ����
		*�Ա�ʹ�ôӶ���̹߳��Ӧ�������Լ���ͬ��ʹ���α�ʱ�Ĵ��롣
		*ʵ��Ӧ�ü̳�AbstractCursor*/
		List<Province> listprovince=new ArrayList<Province>();
		
		Cursor cursor=db.query("province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
			//�����ݷ�װ��province�������棬��listΪ���ز�ѯ�����
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
	 * ��cityʵ���洢�����ݿ�
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
	 * ��ĳʡ������м����ݵĲ�ѯ
	 */
	public List<City> loadcity(int provinceid){
		List<City> listcity=new ArrayList<City>();
		//���м����ݿ���ж���ĳһ��ݵ�����
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
	 * ��countyʵ����
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
	 * �����ݿ��ȡ�м��µ������س�
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
