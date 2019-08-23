package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class FindWriteCode extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_write_code);
        findViewById(R.id.find_btn).setOnClickListener(FindBtn);
        findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Function.class));
            }
        });
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SelectFind.class));
            }
        });
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SelectFind.class));
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
        startActivity(new Intent(getBaseContext(), SelectFind.class));
    }

    public View.OnClickListener FindBtn = new View.OnClickListener() {
        public void onClick(View v) {
            if (((Switch) findViewById(R.id.find_at_lm_swc)).isChecked())
                Memory.getMetadataBuffer("LC" + ((TextView) findViewById(R.id.code_txt)).getText() + "OT" + Memory.otdel).continueWithTask(task -> {
                    if (task.getResult().getCount() > 0) {
                        if (task.getResult().getCount() < 2)
                            Memory.openReadFile(task.getResult()).continueWithTask(task1 -> {
                                Memory.tovarData = task1.getResult();
                                Memory.newFile =  ((TextView)findViewById(R.id.code_txt)).getText().toString();
                                startActivity(new Intent(getBaseContext(), ContentData.class));
                                return null;

                            });
                        else {
                            Memory.MB = task.getResult();
                            startActivity(new Intent(getBaseContext(), SelectedMB.class));
                        }
                    } else
                        startActivity(new Intent(getBaseContext(), NotFind.class));
                    return null;
                });
            else
                try {
                    Memory.getMetadataBuffer("LC" + Memory.dataBaseOtdel.tovars.get(Memory.dataBaseOtdel.findAtBarcode(((TextView) findViewById(R.id.code_txt)).getText().toString())).lmcode + "OT" + Memory.otdel).continueWithTask(task -> {
                        if (task.getResult().getCount() > 0) {
                            if (task.getResult().getCount() < 2)
                                Memory.openReadFile(task.getResult()).continueWithTask(task1 -> {
                                    Memory.tovarData = task1.getResult();
                                    Memory.newFile = Memory.dataBaseOtdel.tovars.get(Memory.dataBaseOtdel.findAtBarcode(((TextView) findViewById(R.id.code_txt)).getText().toString())).lmcode;
                                    startActivity(new Intent(getBaseContext(), ContentData.class));
                                    return null;

                                });
                            else {
                                Memory.MB = task.getResult();
                                startActivity(new Intent(getBaseContext(), SelectedMB.class));
                            }
                        } else
                            startActivity(new Intent(getBaseContext(), NotFind.class));
                        return null;
                    });
                } catch (ArrayIndexOutOfBoundsException ex) {
                    startActivity(new Intent(getBaseContext(), NotFind.class));
                }
        }
    };
}