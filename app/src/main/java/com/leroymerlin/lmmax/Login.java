package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), Function.class));
			}
		});
		findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), Function.class));
			}
		});

		findViewById(R.id.confirm_btn).setOnClickListener(Confirm);
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(getBaseContext(), Function.class));
	}

	private View.OnClickListener Confirm = new View.OnClickListener() {
		public void onClick(View v) {
			if (Memory.pass_settings.replace("\n", "").equals(((EditText) findViewById(R.id.editText)).getText().toString())) {
				startActivity(new Intent(getBaseContext(), Settings.class));
				finish();
			} else
				((TextView) findViewById(R.id.textViewError)).setText("Неверный пароль");
		}
	};
}