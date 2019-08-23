package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.drive.MetadataBuffer;

public class Success extends Activity {

	int k = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_success);
		l = 0;
		try {
			k = Memory.dataBaseOtdel.findAtLmcode(Memory.newFile);
			Memory.showMessage(getBaseContext(), k + "");
			((TextView) findViewById(R.id.tovar_name_lbl)).setText("Товар: " + Memory.dataBaseOtdel.tovars.get(k).name_tovar);
		} catch (NullPointerException ex) {
			Memory.showMessage(getBaseContext(), "Excel база не найдена");
			finish();
			startActivity(new Intent(getBaseContext(), NotFind.class));
		} catch (ArrayIndexOutOfBoundsException ex) {
			finish();
			startActivity(new Intent(getBaseContext(), NotFind.class));
		}

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

		findViewById(R.id.okey_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Memory.getMetadataBuffer("LC" + Memory.newFile + "OT" + Memory.otdel).continueWith(task -> {
					if (task.getResult().getCount() > 0) {
						l = 0;
						MB = task.getResult();
						CheckAddress();
					} else {
						Memory.createFile("LC" + Memory.newFile + "OT" + Memory.otdel, Memory.newFile + "\n" +
								Memory.dataBaseOtdel.tovars.get(k).barcode + "\n" +
								Memory.dataBaseOtdel.tovars.get(k).name_tovar + "\n" +
								((TextView) findViewById(R.id.kolvo)).getText() + "\n" +
								Memory.Address);
						//Memory.AddToT("LC" + Memory.dataBaseOtdel.tovars.get(k).lmcode + "OT" + Memory.otdel);
						Memory.showMessage(getBaseContext(), "Размещено");
					}
					return null;
				});
				finish();
			}
		});
	}

	int l = 0;
	MetadataBuffer MB;

	private void CheckAddress() {
		if (MB.getCount() > l) {
			Memory.getTextFromFile(MB.get(l).getDriveId().asDriveFile()).continueWith(task -> {
				if (Memory.Address.replace("\n", "").equals(task.getResult().split("\n")[4])) {
					Memory.replaceFile(MB, Memory.newFile + "\n" +
							Memory.dataBaseOtdel.tovars.get(k).barcode + "\n" + Memory.dataBaseOtdel.tovars.get(k).name_tovar + "\n" +
							(Integer.parseInt(task.getResult().split("\n")[3]) + Integer.parseInt(((TextView) findViewById(R.id.kolvo)).getText().toString())) + "\n" +
							Memory.Address.replace("\n", ""), l);
					Memory.showMessage(getBaseContext(), "Добавлено " + ((TextView) findViewById(R.id.kolvo)).getText().toString() + " ед. товара");
				} else {
					l++;
					CheckAddress();
				}
				return null;
			});
		} else {
			Memory.createFile("LC" + Memory.newFile + "OT" + Memory.otdel, Memory.newFile + "\n" +
					Memory.dataBaseOtdel.tovars.get(k).barcode + "\n" +
					Memory.dataBaseOtdel.tovars.get(k).name_tovar + "\n" +
					((TextView) findViewById(R.id.kolvo)).getText() + "\n" +
					Memory.Address);
			//Memory.AddToT("LC" + Memory.dataBaseOtdel.tovars.get(k).lmcode + "OT" + Memory.otdel);
			Memory.showMessage(getBaseContext(), "Размещено");
		}
	}
}