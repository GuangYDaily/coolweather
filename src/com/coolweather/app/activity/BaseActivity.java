
package com.coolweather.app.activity;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ActivityCollector.addactivity(this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ActivityCollector.removeactivity(this);
	}
}
