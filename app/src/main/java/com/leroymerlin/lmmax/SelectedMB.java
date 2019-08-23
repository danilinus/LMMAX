package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class SelectedMB extends Activity {

	LinearLayout Arr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_selected);

		findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), Function.class));
			}
		});
		findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), AddressStorage.class));
			}
		});
		Arr = findViewById(R.id.array_buttons);
	}

	public void AddButton(int k) {
		Memory.getTextFromFile(Memory.MB.get(k).getDriveId().asDriveFile()).continueWithTask(task ->
		{
			Button b = new Button(this);
			b.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			b.setText("Адрес " + task.getResult().split("\n")[4] + " | кол-во: " + task.getResult().split("\n")[3]);
			b.setTag(k);
			b.setOnClickListener(SelectedBtn);
			Arr.addView(b);
			return null;
		});
	}

	@Override
	public void onResume() {
		super.onResume();

		Arr.removeAllViews();

		for (int i = 0; i < Memory.MB.getCount(); i++)
			AddButton(i);
	}

	private View.OnClickListener SelectedBtn = new View.OnClickListener() {
		public void onClick(View v) {
			Memory.openReadFile(Memory.MB, (int) v.getTag()).continueWithTask(task -> {
				Memory.tovarData = task.getResult();
				finish();
				startActivity(new Intent(getBaseContext(), ContentData.class));
				return null;
			});
		}
	};
}
