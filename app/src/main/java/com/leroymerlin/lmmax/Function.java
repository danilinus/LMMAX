package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class Function extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);
        ((TextView) findViewById(R.id.text_func)).setText("Функции отдела №" + Memory.otdel.replace(Memory.city, ""));
        findViewById(R.id.settings_btn).setOnClickListener(SettingsClickBtn);
        findViewById(R.id.address_storage_btn).setOnClickListener(AddressStorageClickBtn);
        findViewById(R.id.find_tovar_btn).setOnClickListener(FindClickBtn);
        findViewById(R.id.home_btn).setOnClickListener(SelectOtdelBtn);
        findViewById(R.id.back_btn).setOnClickListener(SelectOtdelBtn);

        Memory.UpdateTovarBase();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), Input.class));
    }

    private View.OnClickListener AddressStorageClickBtn = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(), AddressStorage.class));
        }
    };

    private View.OnClickListener SettingsClickBtn = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(), Login.class));
        }
    };

    private View.OnClickListener FindClickBtn = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(), SelectFind.class));
        }
    };

    private View.OnClickListener SelectOtdelBtn = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(), Input.class));
        }
    };
}