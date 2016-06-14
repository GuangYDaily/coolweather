
package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

public class ActivityCollector extends Activity {
	public static List<Activity> activitys=new ArrayList<Activity>();
	public static void  addactivity(Activity activity){
		activitys.add(activity);

	}
	
	public static void removeactivity(Activity activity){
		activitys.remove(activity);
	}
	
	public static void finishall(){
		for(Activity activity:activitys){
			activity.finish();
		}
	}
}
