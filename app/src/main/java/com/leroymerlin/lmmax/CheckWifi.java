package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;

public class CheckWifi extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_wifi);
		if (getCurrentSsid(getBaseContext()).equals("GROUPEADEO") || getCurrentSsid(getBaseContext()).equals("WF") || getCurrentSsid(getBaseContext()).equals("Xyu"))
			startActivity(new Intent(this, SignInGooglePage.class));
		findViewById(R.id.repeat).setOnClickListener(RepeatButton);

	}

	private View.OnClickListener RepeatButton = new View.OnClickListener() {
		public void onClick(View v) {
			if (getCurrentSsid(getBaseContext()).equals("GROUPEADEO") || getCurrentSsid(getBaseContext()).equals("WF") || getCurrentSsid(getBaseContext()).equals("Xyu"))
				startActivity(new Intent(getBaseContext(), SignInGooglePage.class));
		}
	};

	private String getCurrentSsid(Context context) {
		String ssid = "";
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo.isConnected()) {
			final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
			if (connectionInfo != null)
				ssid = connectionInfo.getSSID().replace("\"", "");
		}
		return ssid;
	}
}
