
package com.coolweather.app.model;

/**
* @ClassName: Country
* @����: TODO
* @����:���ؼ����ݳ�Ա���з�װ
* @���� zhangguang
* @date 2016-4-8 ����9:26:31
*
*/
public class County {
	//��Ա����
	private int id;
	private String countyName;
	private String countyCode;
	private int cityid;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCountyName() {
		return countyName;
	}
	public void setCountyName(String countName) {
		this.countyName = countName;
	}
	public String getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(String countCode) {
		this.countyCode = countCode;
	}
	public int getCityid() {
		return cityid;
	}
	public void setCityid(int cityid) {
		this.cityid = cityid;
	}
	
}
