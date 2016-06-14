
package com.coolweather.app.model;

/**
* @ClassName: City
* @描述: TODO
* @作用:对市级数据成员进行封装
* @作者 zhangguang
* @date 2016-4-8 下午9:24:44
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
