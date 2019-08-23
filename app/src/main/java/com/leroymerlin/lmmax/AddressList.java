package com.leroymerlin.lmmax;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AddressList extends Activity {

	AlertDialog.Builder dlgAlert;
	EditText input;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_list_redesign);
		findViewById(R.id.cancel_btn).setOnClickListener(CancelBtn);


		dlgAlert = new AlertDialog.Builder(this, R.style.LightDialogTheme);
		dlgAlert.setTitle("Введите адреса которые следует добавить");
		dlgAlert.setPositiveButton("ВВЕЛ", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Memory.UpdateTovarBase().continueWithTask(task ->
				{
					for (String str : Memory.sortAddress(Memory.removeDoubleSpace(input.getText().toString())).split(" "))
						if (!Memory.tovarsBaseOtdel.check(str))
							Memory.tovarsBaseOtdel.add(str);
					Memory.LoadToCloudTovarBase().continueWithTask(task1 ->
					{
						SyncAddressList();
						return null;
					});
					return null;
				});
			}
		});
		findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				input = new EditText(getBaseContext());
				input.setTextColor(getResources().getColor(R.color.PrimaryDark));
				dlgAlert.setView(input);
				dlgAlert.create().show();
			}
		});

		((EditText) findViewById(R.id.find_text)).addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					List<String> o = new ArrayList<>();
					String[] m = Memory.tovarsBaseOtdel.get().split(" ");
					for (int i = 0; i < m.length; i++)
						if (m[i].contains(s.toString()))
							o.add(m[i]);
					((ListView) findViewById(R.id.address_list)).setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, o));
				} else
					SyncAddressList();
			}
		});

		findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), Function.class));
			}
		});
		findViewById(R.id.remove_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), RemoveAddress.class));
			}
		});
		findViewById(R.id.back_btn).setOnClickListener(CancelBtn);
		SyncAddressList();
	}

	private View.OnClickListener CancelBtn = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			finish();
		}
	};

	public void SyncAddressList() {
		Memory.UpdateTovarBase().continueWithTask(task ->
		{
			if (Memory.tovarsBaseOtdel.addressList.size() > 0)
				((ListView) findViewById(R.id.address_list)).setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Memory.tovarsBaseOtdel.get().split(" ")));
			else
				((ListView) findViewById(R.id.address_list)).setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[0]));
			return null;
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		SyncAddressList();
	}
}
