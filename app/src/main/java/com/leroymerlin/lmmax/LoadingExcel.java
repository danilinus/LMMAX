package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.Task;

import java.io.InputStream;

public class LoadingExcel extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading_excel);
		new Thread(new Runnable() {
			@Override
			public void run() {
				getExcelData().continueWith(task -> {
					Memory.dataBaseOtdel = ((BaseOtdel) Memory.ConvertToObject(task.getResult()));
					startActivity(new Intent(getBaseContext(), Function.class));
					Memory.showMessage(getBaseContext(), "Успешно загружено");
					return null;
				});
			}
		}).start();
	}

	public Task<InputStream> getExcelData() {
		return Memory.getMetadataBuffer("XLTOTD" + Memory.otdel).continueWithTask(task -> {
			return Memory.openReadFile("XLTOTD" + Memory.otdel).continueWith(task1 -> {
				return task1.getResult().getInputStream();
			});
		});
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(getBaseContext(), Input.class));
	}
}
