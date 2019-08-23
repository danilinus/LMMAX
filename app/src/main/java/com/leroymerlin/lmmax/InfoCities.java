package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoCities extends Activity {

	LinearLayout backspace;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_cities);
		findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				startActivity(new Intent(getBaseContext(), Function.class));
			}
		});
		findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		backspace = findViewById(R.id.back_lin);

		Memory.getMetadataBuffer("infsys").continueWith(task ->
		{
			if (task.getResult().getCount() > 0) {
				Memory.getTextFromFile("infsys").continueWith(task1 -> {
					String[] g = task1.getResult().replace("_\n", "").split("_");

					for (int i = 0; i < g.length; i += 3) {
						LinearLayout l = new LinearLayout(this);
						l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
						l.setOrientation(LinearLayout.VERTICAL);
						l.setTag(i);
						l.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								Memory.inf = g[(int) view.getTag()];
								startActivity(new Intent(getBaseContext(), InfoOtdels.class));
							}
						});

						LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT
						);
						params.setMargins((int) getResources().getDisplayMetrics().density * 20, (int) getResources().getDisplayMetrics().density * 10, 0, 0);

						TextView tName = new TextView(this);
						tName.setTextSize(18);
						tName.setTextColor(Color.parseColor("#000000"));
						tName.setLayoutParams(params);
						tName.setText("НОМЕР МАГАЗИНА: " + g[i]);
						l.addView(tName);

						params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT
						);
						params.setMargins((int) getResources().getDisplayMetrics().density * 40, 0, 0, 0);

						tName = new TextView(this);
						tName.setTextSize(12);
						tName.setLayoutParams(params);
						tName.setText("Последний вход: " + g[i + 2]);
						l.addView(tName);

						params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.WRAP_CONTENT
						);
						params.setMargins((int) getResources().getDisplayMetrics().density * 40, 0, 0, 0);

						tName = new TextView(this);
						tName.setTextSize(12);
						tName.setLayoutParams(params);
						tName.setLeft((int) getResources().getDisplayMetrics().density * 40);
						tName.setText("Кол-во входов: " + g[i + 1]);
						l.addView(tName);

						params = new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								2
						);
						params.setMargins((int) getResources().getDisplayMetrics().density * 30, (int) getResources().getDisplayMetrics().density * 8, (int) getResources().getDisplayMetrics().density * 30, 0);

						LinearLayout k = new LinearLayout(this);
						k.setLayoutParams(params);
						k.setLeft((int) getResources().getDisplayMetrics().density * 30);
						k.setRight((int) getResources().getDisplayMetrics().density * 30);
						k.setBackgroundColor(Color.parseColor("#39b44b"));
						l.addView(k);

						backspace.addView(l);
					}

					return null;
				});
			}
			return null;
		});
	}
}
