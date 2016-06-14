
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
		//��ס���뱾�����ݿ�
		SharedPreferences prf;
		SharedPreferences.Editor editor;
		//��ס���밴ť
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
			
			//��load����ʵ����
			loaddb=new LoadDB(this);
			//��ס���빦��
			prf=PreferenceManager.getDefaultSharedPreferences(this);
			//�Զ���¼
			autolong=(CheckBox) findViewById(R.id.checkautologin_id);
			
			rememberpass=(CheckBox) findViewById(R.id.checkbox_id);
			Boolean isremeber=prf.getBoolean("remember", false);
			Boolean isautologin=prf.getBoolean("autologin", false);
			//���Ϊ�����ס���룬ֱ����д

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
				alert.setTitle("�Զ���¼");
				alert.setMessage("ѡ��һ���Զ���¼,��������");
				alert.setCancelable(false);
				alert.setPositiveButton("�õ�",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						denglu();
					}
				});
				alert.setNegativeButton("������", new DialogInterface.OnClickListener() {
					
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
			//��¼���ܵ�ʵ��
			case R.id.buttonlogin_id:
				String nametextone=nametext.getText().toString();
				String passtextone=passtext.getText().toString();
				int result=loaddb.loginweather(nametextone, passtextone);
				if(result==0){
					Toast.makeText(this, "�û���������", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(this, "�������", 6).show();
					passtext.setText("");
				}	
				break;
			//�����ɹ��ܵ�ʵ��
			case R.id.buttonload_id:
					Intent intent=new Intent(this,LoadActivity.class);
					startActivity(intent);
					break;
			//�������ǵĹ���ʵ��
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
			pro.setMessage("�Զ���¼��");
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
			alertdialog.setTitle("���ڿ���");
			alertdialog.setMessage("��������⹦�ܲ����ɵ�һ�д����ṩ����¼��ע�Ṧ���ɱ��˶�����ɣ���Ϊ��һ���ɹ���android��Ŀ�����ڱ���ˮƽ���ޣ��кܶ಻����ĵط��������£�лл����֧��");
			alertdialog.setCancelable(true);
			alertdialog.show();
		}
}
