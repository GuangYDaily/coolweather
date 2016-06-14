
package com.coolweather.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.Person;



public class LoadDB {
	SQLiteDatabase db;
	public LoadDB(Context context){
		Loadopenhelper dbopenhelper=new Loadopenhelper(context, "load",null, 1);
		db=dbopenhelper.getWritableDatabase();
	}
	
	public boolean saveweather(Person person) {
		if(person!=null){
			ContentValues values=new ContentValues();
			values.put("name", person.getName());
			values.put("password", person.getPassword());
			values.put("email", person.getEmail());
			db.insert("load", null, values);
			return true;
		}
		return false;
	}
	
	public int loginweather(String name,String password){
		String pass=null;
		Cursor cursor=db.query("load", null, "name=?", new String[] {name}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				pass=cursor.getString(cursor.getColumnIndex("password"));
			}while(cursor.moveToNext());
		}
		if(pass!=null){
			if(pass.equals(password)){
				return 1;
			}else{
				return 2;
			}
		}else{
			return 0; 
		}
	}
	
	public boolean isexits(String name){
		Cursor cursor=db.query("load", null, "name=?", new String[]{name}, null, null, null);
		if(cursor.moveToFirst()){
			return true;
		}
		return false;
	}
	
}

