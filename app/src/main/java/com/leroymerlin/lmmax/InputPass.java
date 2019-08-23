package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class InputPass extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		findViewById(R.id.confirm_btn).setOnClickListener(Confirm);
	}

	private View.OnClickListener Confirm = new View.OnClickListener() {
		public void onClick(View v) {
			if (((EditText) findViewById(R.id.editText)).getText().toString().equals("Passwd91")) {
				startActivity(new Intent(getBaseContext(), InputDel.class));
				finish();
			} else
				((TextView) findViewById(R.id.textViewError)).setText("Неверный пароль");
		}
	};
}