package com.leroymerlin.lmmax;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class RemoveAddress extends Activity {

	LinearLayout ContentAddress;
	Button removeButton;
	AlertDialog.Builder dlgAlert;
	String s;
	CategoryListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_address);

		findViewById(R.id.cancel_btn).setOnClickListener(CancelBtn);
		findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), Function.class));
			}
		});
		findViewById(R.id.back_btn).setOnClickListener(CancelBtn);
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
					m = new String[o.size()];
					for (int i = 0; i < o.size(); i++)
						m[i] = o.get(i);
					adapter = new CategoryListAdapter(m, getBaseContext(), new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							removeButton.setEnabled(((CategoryListAdapter) ((ListView) findViewById(R.id.address_list)).getAdapter()).isSelected());
						}
					});
					((ListView) findViewById(R.id.address_list)).setAdapter(adapter);
				} else {
					String[] address_s = Memory.tovarsBaseOtdel.get().split(" ");
					adapter = new CategoryListAdapter(address_s, getBaseContext(), new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							removeButton.setEnabled(((CategoryListAdapter) ((ListView) findViewById(R.id.address_list)).getAdapter()).isSelected());
						}
					});
					((ListView) findViewById(R.id.address_list)).setAdapter(adapter);
				}
			}
		});
		removeButton = findViewById(R.id.remove_btn);
		dlgAlert = new AlertDialog.Builder(this, R.style.LightDialogTheme);
		dlgAlert.setTitle("Внимание!");
		dlgAlert.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String sl = "";
				for (int i = 0; i < Memory.tovarsBaseOtdel.get().split((" ")).length; i++) {
					boolean b = false;
					for (int j = 0; j < s.split(", ").length; j++)
						if (s.split(", ")[j].equals(Memory.tovarsBaseOtdel.get().replace("\n", "").split((" "))[i])) {
							b = true;
							break;
						}
					if (!b) sl += Memory.tovarsBaseOtdel.get().split((" "))[i] + " ";
				}
				if (sl != null && sl.length() > 0)
					sl = sl.substring(0, sl.length() - 1);
				Memory.getTextFromFile("ADROTD" + Memory.otdel).continueWithTask(task -> {
					String str = "";

					for (int i = 0; i < task.getResult().replace("\n", "").split((" ")).length; i++) {
						boolean b = false;
						for (int j = 0; j < s.split(", ").length; j++)
							if (s.split(", ")[j].equals(task.getResult().replace("\n", "").split((" "))[i])) {
								b = true;
								break;
							}
						if (!b) str += task.getResult().replace("\n", "").split((" "))[i] + " ";
					}
					if (str != null && str.length() > 0)
						str = str.substring(0, str.length() - 1);

					Memory.replaceFile("ADROTD" + Memory.otdel, str);
					return null;
				});
				finish();
			}
		});
		removeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				s = ((CategoryListAdapter) ((ListView) findViewById(R.id.address_list)).getAdapter()).getSelected();
				dlgAlert.setMessage("Это дейстиве удалит все товары с адресов: " + s + ". Вы уверены?");
				dlgAlert.create().show();
			}
		});

        /*Memory.getAddressList().continueWithTask(task -> {
            Memory.address_list = task.getResult();
            String[] address_s = task.getResult().replace("\n", "").split(" ");

            adapter = new CategoryListAdapter(address_s, this, new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    removeButton.setEnabled(((CategoryListAdapter)((ListView)findViewById(R.id.address_list)).getAdapter()).isSelected());
                }
            });
            ((ListView)findViewById(R.id.address_list)).setAdapter(adapter);
            return null;
        });*/
	}

	private View.OnClickListener CancelBtn = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			finish();
		}
	};
}
