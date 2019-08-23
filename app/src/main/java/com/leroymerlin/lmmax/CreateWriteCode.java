package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class CreateWriteCode extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_write_code);
        ((TextView) findViewById(R.id.tvt_lbl)).setText("Адрес: " + Memory.Address);
        findViewById(R.id.find_btn).setOnClickListener(FindBtn);
        findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Function.class));
            }
        });
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SelectCreate.class));
            }
        });
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SelectCreate.class));
            }
        });
        ((Switch) findViewById(R.id.find_at_lm_swc)).setChecked(Memory.findLm);
        ((Switch) findViewById(R.id.find_at_lm_swc)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Memory.findLm = isChecked;
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), SelectCreate.class));
    }

    public View.OnClickListener FindBtn = new View.OnClickListener() {
        public void onClick(View v) {
            try {
                if (((Switch) findViewById(R.id.find_at_lm_swc)).isChecked()) {
                    Memory.newFile = ((TextView) findViewById(R.id.code_txt)).getText().toString();
                    startActivity(new Intent(getBaseContext(), Success.class));
                } else {
                    Memory.newFile = Memory.dataBaseOtdel.tovars.get(Memory.dataBaseOtdel.findAtBarcode(((TextView) findViewById(R.id.code_txt)).getText().toString())).lmcode;
                    startActivity(new Intent(getBaseContext(), Success.class));
                }
            } catch (NullPointerException ex) {
                startActivity(new Intent(getBaseContext(), NotFind.class));
            } catch (ArrayIndexOutOfBoundsException ex) {
                startActivity(new Intent(getBaseContext(), NotFind.class));
            }
        }
    };
}