
package com.coolweather.app.receiver;

import com.coolweather.app.service.AutoUpdateserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AutoUpdatereceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent intents=new Intent(context,AutoUpdateserver.class);
		context.startService(intents);
	}

}
