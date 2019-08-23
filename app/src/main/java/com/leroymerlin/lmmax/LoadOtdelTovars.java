package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class LoadOtdelTovars extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading_excel);
		((TextView) findViewById(R.id.load_lbl)).setText("Загрузка списка товаров");

		//Memory.SyncAddress();

		Memory.getMetadataBuffer("tvrlst" + Memory.otdel).continueWith(task ->
		{
			if (task.getResult().getCount() > 0)
				Memory.getTextFromFile("tvrlst" + Memory.otdel).continueWith(task1 -> {
					Memory.g = task1.getResult().split("\n");
					startActivity(new Intent(this, ListTovarsOtdel.class));
					finish();
					return null;
				});
			else {
				startActivity(new Intent(this, NotFind.class));
				finish();
			}
			return null;
		});

	}

	protected void showMessage(String message) {
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}
}