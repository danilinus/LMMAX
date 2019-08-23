package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddressStorage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_storage);

		findViewById(R.id.map_otdel_btn).setOnClickListener(MapOtdelBtn);
		findViewById(R.id.get_product_btn).setOnClickListener(DostBtn);
		findViewById(R.id.place_product_btn).setOnClickListener(PlaceBtn);
		findViewById(R.id.list_tovars_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getBaseContext(), LoadTovars.class));
			}
		});
		findViewById(R.id.list_otd_tovars_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(getBaseContext(), AddressSelectedOtdel.class));
			}
		});
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
	}

	@Override
	public void onBackPressed() {
		startActivity(new Intent(getBaseContext(), Function.class));
	}


	private View.OnClickListener MapOtdelBtn = new View.OnClickListener() {
		public void onClick(View v) {
			startActivity(new Intent(getBaseContext(), SeePhoto.class));
		}
	};

	private View.OnClickListener DostBtn = new View.OnClickListener() {
		public void onClick(View v) {
			startActivity(new Intent(getBaseContext(), SelectFind.class));
		}
	};

	private View.OnClickListener PlaceBtn = new View.OnClickListener() {
		public void onClick(View v) {
			startActivity(new Intent(getBaseContext(), AddressSelected.class));
		}
	};

}
