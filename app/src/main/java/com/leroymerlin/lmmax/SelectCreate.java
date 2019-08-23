package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SelectCreate extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_find);

		findViewById(R.id.write_code_btn).setOnClickListener(WriteCodeBtn);
		findViewById(R.id.scan_barcode_btn).setOnClickListener(ScanBarcodeBtn);
		((TextView) findViewById(R.id.sel_lbl)).setText("ВЫБЕРИТЕ ВАРИАНТ РАЗМЕЩЕНИЯ\nАДРЕС: " + Memory.Address);
		findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), Function.class));
			}
		});
		findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(), AddressSelected.class));
			}
		});
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(getBaseContext(), Function.class));
	}

	public View.OnClickListener WriteCodeBtn = new View.OnClickListener() {
		public void onClick(View v) {
			startActivity(new Intent(getBaseContext(), CreateWriteCode.class));
		}
	};

	public View.OnClickListener ScanBarcodeBtn = new View.OnClickListener() {
		public void onClick(View v) {
			startActivity(new Intent(getBaseContext(), PlaceScanBarcode.class));
		}
	};
}
