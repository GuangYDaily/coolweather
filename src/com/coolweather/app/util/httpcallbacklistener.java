
package com.coolweather.app.util;

public interface httpcallbacklistener {
	void onFinish(String response);
	void onError(Exception e);
}
