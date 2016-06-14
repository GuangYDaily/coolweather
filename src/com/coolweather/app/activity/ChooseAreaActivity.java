
package com.coolweather.app.activity;

import java.util.ArrayList;

import java.util.List;

import com.com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
import com.coolweather.app.util.httpcallbacklistener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
* @ClassName: ChooseAreaActivity
* @����: TODO
* @����:����ʡ���س��еĻ��
* @���� zhangguang
* @date 2016-4-9 ����7:36:41
*
*/
public class ChooseAreaActivity extends BaseActivity implements OnItemClickListener {
	/**
	 * �ж���Ϣ�Ƿ��Ǵ�weatheractivity��������
	 */
	private boolean isformweatheracticity;
	
	/**
	 * ����״̬�ľ�̬����
	 */
	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY=1;
	public static final int LEVEL_COUNTY=2;
	//����ǰ����״̬
	private int currentlevel;
	/**
	 * ʡ�����б�
	 */
	private List<Province>  provincelist;
	private List<City>      citylist;
	private List<County>    countylist;
	
	/**
	 * ѡ�еĳ���
	 */
	private Province selectedprovince;
	private City selectescity;
	/**
	 * ���������ı����б�arrayadapter��������db��,listview���������б���
	 */
	private ProgressDialog progressdialog;
	private TextView textview;
	private ListView listview;
	private ArrayAdapter<String> adapt;
	private CoolWeatherDB coolWeatherDBzg;
	private List<String> datalist=new ArrayList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listview=(ListView)findViewById(R.id.listview_id);
		textview=(TextView) findViewById(R.id.title_textid);
		adapt=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datalist);
		listview.setAdapter(adapt);
		listview.setOnItemClickListener(this);
		coolWeatherDBzg=CoolWeatherDB.getinstance(this);
		queryprovince();
		
		/**
		 * ��ȡ��weatheractivity���ݹ�����ֵ
		 */
		isformweatheracticity=getIntent().getBooleanExtra("from_weather_city", false);
		
		
		/**
		 * ��ת��weatheractivity�¼���
		 */
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		//�����ȡ��sharedpreferences�����ֵ����ֱ����ת
		if(prefs.getBoolean("city_selected", false)&&!isformweatheracticity){
			Intent intent=new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
			return ;
		}
	}
	/**
	 * �����Ļ�ᴥ����listview�¼�
	 */

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(currentlevel==LEVEL_PROVINCE){
			selectedprovince=provincelist.get(position);
			querycities();
		}else if(currentlevel==LEVEL_CITY){
			selectescity=citylist.get(position);
			querycounties();
		}else if(currentlevel==LEVEL_COUNTY){
			//�����ǰ�������ؼ����Ϳ�ʼ��ת������ҳ��
			String countycode=countylist.get(position).getCountyCode();
			Intent intent=new Intent(ChooseAreaActivity.this,WeatherActivity.class);
			intent.putExtra("county_code", countycode);
			startActivity(intent);
			finish();
		}
	}
	
	/**
	 * ��ѯʡ������
	 */
	private void queryprovince() {
		// ���ʡ����ѯ����
		provincelist=coolWeatherDBzg.loadprovince();
		
		if(provincelist.size()>0){
			//�������������¼���
			datalist.clear();
			for(Province province:provincelist){
				datalist.add(province.getProvinceName());
			}
			
			adapt.notifyDataSetChanged();
			//���ý���
			listview.setSelection(0);
			textview.setText("�й�");
			currentlevel=LEVEL_PROVINCE;
		}else{
			queryFromServer(null,"province");
		}
	}
	
	/**
	 * ��ѯ�м�����
	 */
	private void querycities() {
		// TODO Auto-generated method stub
		citylist=coolWeatherDBzg.loadcity(selectedprovince.getId());
			if(citylist.size()>0){
				datalist.clear();
				for(City city:citylist){
					datalist.add(city.getCityName());
				}
				adapt.notifyDataSetChanged();
				listview.setSelection(0);
				textview.setText(selectedprovince.getProvinceName());
				currentlevel=LEVEL_CITY;
			}else{
				queryFromServer(selectedprovince.getProvinceCode(),"city");
			}
	}
	/**
	 * ��ѯ�ؼ�����
	 */
	private void querycounties(){
		// TODO Auto-generated method stub
		countylist=coolWeatherDBzg.loadcounty(selectescity.getId());
		if(countylist.size()>0){
			datalist.clear();
			for(County county:countylist){
				datalist.add(county.getCountyName());
			}
			adapt.notifyDataSetChanged();
			listview.setSelection(0);
			textview.setText(selectescity.getCityName());
			currentlevel=LEVEL_COUNTY;
		}else{
			queryFromServer(selectescity.getCityCode(),"county");
		}
	} 
	
	
	/**
	 * ���ݴ�����ź����ʹӷ������ϲ�ѯʡ�����鼮
	 */
	private void queryFromServer(final String code,final String type) {
		// TODO Auto-generated method stub
		String address;
		if(!TextUtils.isEmpty(code)){
			address="http://www.weather.com.cn/data/list3/city"+code+".xml";
		}else{
			address="http://www.weather.com.cn/data/list3/city.xml";
		}
		//��������ʾ
		showprogressdialog();
		HttpUtil.sendHttpResquest(address, new httpcallbacklistener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result=false;
				if("province".equals(type)){
					//����ʡ�����������ص����ݣ�����util
					result=Utility.handleprovincesResponse(coolWeatherDBzg, response);
				}else if("city".equals(type)){
					result=Utility.handlecityresponse(coolWeatherDBzg, response, selectedprovince.getId());
				}else if("county".equals(type)){
					result=Utility.handlecountyresponse(coolWeatherDBzg, response, selectescity.getId());
				}
				//������β�ѯ���ɹ����ٴβ�ѯ
				if(result){
					runOnUiThread(new  Runnable() {
						@Override
						public void run() {
								closeprogressDialog();
								if("province".equals(type)){
									queryprovince();
								}else if("city".equals(type)){
									querycities();
								}else if("country".equals(type)){
									querycounties();
								}
						}	
					});
				}
			}
			
			/**
			 * ��ѯʧ�ܣ��رս���������������ʧ����ʾ
			 */
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						closeprogressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_LONG).show();
						
					}
				});
			}
		});
		
		
	}
	
	
	
	/**
	 * ���ؽ�����
	 */
	private void showprogressdialog() {
		// TODO Auto-generated method stub
		if(progressdialog==null){
			progressdialog=new ProgressDialog(ChooseAreaActivity.this);
			progressdialog.setMessage("���ڼ���");
			progressdialog.setCancelable(false);
			progressdialog.show();
		}
	}
	/**
	 * ������������ʧ
	 */
	private void closeprogressDialog() {
		// TODO Auto-generated method stub
		if(progressdialog!=null){
			progressdialog.dismiss();
		}
	}
	
	/**
	 * ���˷�Ӧ
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(currentlevel==LEVEL_COUNTY){
			querycities();
		}else if(currentlevel==LEVEL_CITY){
			queryprovince();
		}else{
			if(isformweatheracticity){
				Intent intent=new Intent(this,WeatherActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}
}
