
package com.coolweather.app.activity;

import com.com.coolweather.app.R;
import com.coolweather.app.db.LoadDB;
import com.coolweather.app.model.Person;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LoadActivity extends Activity implements OnClickListener {
	EditText zcname,zcpass,zcemail;
	RadioGroup zcsex;
	RadioButton zcnan,zcnv;
	Button zuceload;
	CheckBox check;
	LoadDB loaddb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.load_layoout);
		zuceload=(Button) findViewById(R.id.zucebutton_id);
		zuceload.setOnClickListener(this);
		zcsex=(RadioGroup) findViewById(R.id.zcsexbutton_id);
		zcnan=(RadioButton) findViewById(R.id.zucenan_id);
		zcnv=(RadioButton) findViewById(R.id.zucenv_id);
		zcname=(EditText) findViewById(R.id.zucename_id);
		zcpass=(EditText) findViewById(R.id.zucepassword_id);
		zcemail=(EditText) findViewById(R.id.zuceemail_id);
		
		
		check=(CheckBox) findViewById(R.id.loadyuedu_id);
		//数据库
		loaddb=new LoadDB(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
			
			savezc();
		
			
		}
	public void alet(){
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setMessage("信息不完整");
		dialog.setCancelable(true);
		dialog.show();
	}
	public void read(){
		AlertDialog.Builder dialog=new AlertDialog.Builder(this);
		dialog.setTitle("你未同意我们的相关协议");
		dialog.setMessage("请选择我已经阅读相关协议");
		dialog.setCancelable(true);
		dialog.show();
	}
	
	
	public void savezc(){
		String name=zcname.getText().toString();
		String pass=zcpass.getText().toString();
		String email=zcemail.getText().toString();
		Person person=null;
		person=new Person();
		person.setName(name);
		person.setPassword(pass);
		person.setEmail(email);
		Boolean res=false;
		res=loaddb.isexits(name);
		if("".equals(name)&&"".equals(pass)){
			alet();
		}else  if(check.isChecked()){
			if(person!=null){
				if(res){
					Toast.makeText(this, "用户名已存在", Toast.LENGTH_LONG).show();
				}
				boolean result=false;
				loaddb.saveweather(person);
				if(result&&!res){
					Toast.makeText(LoadActivity.this, "注册成功", Toast.LENGTH_LONG).show();
					Intent intent=new Intent(this,LoginWeatherActivity.class);
					startActivity(intent);
				}
			}
		}else  {
			read();
		}
	}
}
