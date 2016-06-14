
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
* @描述: TODO
* @作用:遍历省市县城市的活动类
* @作者 zhangguang
* @date 2016-4-9 下午7:36:41
*
*/
public class ChooseAreaActivity extends BaseActivity implements OnItemClickListener {
	/**
	 * 判断消息是否是从weatheractivity穿过来的
	 */
	private boolean isformweatheracticity;
	
	/**
	 * 城市状态的静态常量
	 */
	public static final int LEVEL_PROVINCE=0;
	public static final int LEVEL_CITY=1;
	public static final int LEVEL_COUNTY=2;
	//代表当前城市状态
	private int currentlevel;
	/**
	 * 省市县列表
	 */
	private List<Province>  provincelist;
	private List<City>      citylist;
	private List<County>    countylist;
	
	/**
	 * 选中的城市
	 */
	private Province selectedprovince;
	private City selectescity;
	/**
	 * 进度条，文本，列表，arrayadapter适配器，db类,listview加入数据列表项
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
		 * 获取从weatheractivity传递过来的值
		 */
		isformweatheracticity=getIntent().getBooleanExtra("from_weather_city", false);
		
		
		/**
		 * 跳转到weatheractivity事件中
		 */
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		//如果能取到sharedpreferences里面的值，就直接跳转
		if(prefs.getBoolean("city_selected", false)&&!isformweatheracticity){
			Intent intent=new Intent(this,WeatherActivity.class);
			startActivity(intent);
			finish();
			return ;
		}
	}
	/**
	 * 点击屏幕会触发的listview事件
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
			//如果当前级别是县级，就开始跳转到天气页面
			String countycode=countylist.get(position).getCountyCode();
			Intent intent=new Intent(ChooseAreaActivity.this,WeatherActivity.class);
			intent.putExtra("county_code", countycode);
			startActivity(intent);
			finish();
		}
	}
	
	/**
	 * 查询省级数据
	 */
	private void queryprovince() {
		// 获得省级查询数据
		provincelist=coolWeatherDBzg.loadprovince();
		
		if(provincelist.size()>0){
			//将数据清理重新加入
			datalist.clear();
			for(Province province:provincelist){
				datalist.add(province.getProvinceName());
			}
			
			adapt.notifyDataSetChanged();
			//设置界面
			listview.setSelection(0);
			textview.setText("中国");
			currentlevel=LEVEL_PROVINCE;
		}else{
			queryFromServer(null,"province");
		}
	}
	
	/**
	 * 查询市级数据
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
	 * 查询县级数据
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
	 * 根据传入代号和类型从服务器上查询省市县书籍
	 */
	private void queryFromServer(final String code,final String type) {
		// TODO Auto-generated method stub
		String address;
		if(!TextUtils.isEmpty(code)){
			address="http://www.weather.com.cn/data/list3/city"+code+".xml";
		}else{
			address="http://www.weather.com.cn/data/list3/city.xml";
		}
		//进度条显示
		showprogressdialog();
		HttpUtil.sendHttpResquest(address, new httpcallbacklistener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result=false;
				if("province".equals(type)){
					//解析省级服务器返回的数据，调用util
					result=Utility.handleprovincesResponse(coolWeatherDBzg, response);
				}else if("city".equals(type)){
					result=Utility.handlecityresponse(coolWeatherDBzg, response, selectedprovince.getId());
				}else if("county".equals(type)){
					result=Utility.handlecountyresponse(coolWeatherDBzg, response, selectescity.getId());
				}
				//如果本次查询不成功，再次查询
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
			 * 查询失败，关闭进度条，弹出加载失败提示
			 */
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						closeprogressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_LONG).show();
						
					}
				});
			}
		});
		
		
	}
	
	
	
	/**
	 * 加载进度条
	 */
	private void showprogressdialog() {
		// TODO Auto-generated method stub
		if(progressdialog==null){
			progressdialog=new ProgressDialog(ChooseAreaActivity.this);
			progressdialog.setMessage("正在加载");
			progressdialog.setCancelable(false);
			progressdialog.show();
		}
	}
	/**
	 * 进度条加载消失
	 */
	private void closeprogressDialog() {
		// TODO Auto-generated method stub
		if(progressdialog!=null){
			progressdialog.dismiss();
		}
	}
	
	/**
	 * 后退反应
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
