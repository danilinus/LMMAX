package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteCity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write_city);

		((EditText) findViewById(R.id.city_number_txt)).addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (s.length() == 3) {
					findViewById(R.id.input_btn).setEnabled(true);
				} else {
					findViewById(R.id.input_btn).setEnabled(false);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});


		findViewById(R.id.input_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Memory.city = ((EditText) findViewById(R.id.city_number_txt)).getText().toString();
				Memory.getMetadataBuffer("infsys").continueWith(task ->
				{
					if (task.getResult().getCount() > 0) {
						Memory.getTextFromFile(task.getResult()).continueWith(task1 -> {
							String[] g = task1.getResult().replace("_\n", "").split("_");
							int i;
							String s = "";
							for (i = 0; i < g.length; i += 3)
								if (g[i].contains(Memory.city))
									break;
							if (i >= g.length - 1)
								s = task1.getResult().replace("\n", "") + Memory.city + "_1_" + new SimpleDateFormat("HH:mm dd.MM.yyyy").format(new Date()) + "_";
							else {
								g[i + 1] = (Integer.parseInt(g[i + 1]) + 1) + "";
								g[i + 2] = new SimpleDateFormat("HH:mm dd.MM.yyyy").format(new Date());
								for (i = 0; i < g.length; i++) s += g[i] + '_';
							}
							Memory.replaceFile("infsys", s);
							return null;
						});
					} else
						Memory.createFile("infsys", Memory.city + "_1_" + new SimpleDateFormat("HH:mm dd.MM.yyyy").format(new Date()) + "_");
					return null;
				});
				startActivity(new Intent(getBaseContext(), Input.class));
			}
		});
	}

	@Override
	public void onBackPressed() {
	}
}