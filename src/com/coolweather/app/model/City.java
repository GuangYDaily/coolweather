
package com.coolweather.app.model;

/**
* @ClassName: City
* @����: TODO
* @����:���м����ݳ�Ա���з�װ
* @���� zhangguang
* @date 2016-4-8 ����9:24:44
*
*/
public class City {
	private int id;
	private String cityName;
	private String cityCode;
	private int provinceid;
	public int getProvinceid() {
		return provinceid;
	}
	public void setProvinceid(int provinceid) {
		this.provinceid = provinceid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	
}
