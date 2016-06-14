
package com.coolweather.app.activity;

import com.com.coolweather.app.R;
import com.coolweather.app.db.LoadDB;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginWeatherActivity extends BaseActivity implements OnClickListener { 
		private LoadDB loaddb;
		EditText nametext,passtext;
		Button loginbutton,loadbutton,ofourbutton;
		//记住密码本地数据库
		SharedPreferences prf;
		SharedPreferences.Editor editor;
		//记住密码按钮
		private CheckBox rememberpass,autolong;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.login_layout);
			
			nametext=(EditText) findViewById(R.id.name_id);
			passtext=(EditText) findViewById(R.id.passwors_id);
			loginbutton=(Button) findViewById(R.id.buttonlogin_id);
			loadbutton=(Button) findViewById(R.id.buttonload_id);
			ofourbutton=(Button) findViewById(R.id.buttofour_id);
			
			loginbutton.setOnClickListener(this);
			loadbutton.setOnClickListener(this);
			ofourbutton.setOnClickListener(this);
			
			//将load对象实例化
			loaddb=new LoadDB(this);
			//记住密码功能
			prf=PreferenceManager.getDefaultSharedPreferences(this);
			//自动登录
			autolong=(CheckBox) findViewById(R.id.checkautologin_id);
			
			rememberpass=(CheckBox) findViewById(R.id.checkbox_id);
			Boolean isremeber=prf.getBoolean("remember", false);
			Boolean isautologin=prf.getBoolean("autologin", false);
			//如果为真则记住密码，直接填写

			if(isremeber){
				String name=prf.getString("name", "");
				String pass=prf.getString("password","");
				nametext.setText(name);
				passtext.setText(pass);
				rememberpass.setChecked(true);
				if(isautologin){
					autolong.setChecked(true);
				}
			}
			
			if(isautologin){
				AlertDialog.Builder alert=new AlertDialog.Builder(this);
				alert.setTitle("自动登录");
				alert.setMessage("选择一键自动登录,无需密码");
				alert.setCancelable(false);
				alert.setPositiveButton("好的",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						denglu();
					}
				});
				alert.setNegativeButton("不用了", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
				alert.show();
			}
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			//登录功能的实现
			case R.id.buttonlogin_id:
				String nametextone=nametext.getText().toString();
				String passtextone=passtext.getText().toString();
				int result=loaddb.loginweather(nametextone, passtextone);
				if(result==0){
					Toast.makeText(this, "用户名不存在", Toast.LENGTH_SHORT).show();
					nametext.setText("");
					passtext.setText("");
				}else if(result==1){
					editor=prf.edit();
					if(rememberpass.isChecked()){
						editor.putBoolean("remember", true);
						editor.putBoolean("autologin", true);
						editor.putString("name", nametextone);
						editor.putString("password", passtextone);
					}else {
						editor.clear();
					}
					editor.commit();
					Intent intent=new Intent(this,ChooseAreaActivity.class);
					startActivity(intent);
					finish();
				}else if(result==2){
					Toast.makeText(this, "密码错误", 6).show();
					passtext.setText("");
				}	
				break;
			//点击组成功能的实现
			case R.id.buttonload_id:
					Intent intent=new Intent(this,LoadActivity.class);
					startActivity(intent);
					break;
			//关于我们的功能实现
			case R.id.buttofour_id:
					buttonof();
					break;
			default :
				break;
			}
		}
		
		
		@Override
		protected void onDestroy() {
		// TODO Auto-generated method stub
			super.onDestroy();
			finish();
		}
		
		public void denglu(){
			ProgressDialog pro=new ProgressDialog(this);
			pro.setMessage("自动登录中");
			pro.setCanceledOnTouchOutside(false);
			pro.show();
			
			
			String nametextone=nametext.getText().toString();
			String passtextone=passtext.getText().toString();
			int result=loaddb.loginweather(nametextone, passtextone);
			if(result==1){
				editor=prf.edit();
				if(rememberpass.isChecked()){
					editor.putBoolean("remember", true);
					editor.putBoolean("autologin", true);
					editor.putString("name", nametextone);
					editor.putString("password", passtextone);
				}else {
					editor.clear();
				}
				editor.commit();
				Intent intent=new Intent(this,ChooseAreaActivity.class);
				pro.dismiss();
				startActivity(intent);
				finish();
			}
		}
		
		
		public void buttonof(){
			AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
			alertdialog.setTitle("关于酷软");
			alertdialog.setMessage("本软件主题功能部分由第一行代码提供，登录，注册功能由本人独立完成，作为第一个成功的android项目，由于本人水平有限，有很多不满意的地方，望见谅！谢谢您的支持");
			alertdialog.setCancelable(true);
			alertdialog.show();
		}
}
