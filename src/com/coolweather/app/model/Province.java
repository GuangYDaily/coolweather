
package com.coolweather.app.model;

/**
* @ClassName: Province
* @����: TODO
* @����:��ʡ�����ݳ�Ա���з�װ���������ݰ�ȫ
* @���� zhangguang
* @date 2016-4-8 ����9:22:57
*
*/
public class Province {
	
	private int id;
	private String provinceName;
	private String provinceCode;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
}
