package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Resize extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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
                try {
                    int k = Memory.dataBaseOtdel.findAtLmcode(Memory.newFile);
                    Memory.getTextFromFile(Memory.tovarData.getDriveId().asDriveFile()).continueWithTask(task -> {
                        if (Integer.parseInt(task.getResult().split("\n")[3]) >= Integer.parseInt(((TextView) findViewById(R.id.kolvo)).getText().toString())) {
                            if (task.getResult().split("\n")[3].equals(((TextView) findViewById(R.id.kolvo)).getText().toString())) {
                                Memory.getMetadataBuffer("LC" + Memory.newFile + "OT" + Memory.otdel).continueWithTask(task1 -> {
                                    if (task1.getResult().getCount() < 2)
                                        //Memory.RemAtT("LC" + Memory.newFile + "OT" + Memory.otdel);
                                    Memory.deleteFile(Memory.tovarData);
                                    Memory.showMessage(getBaseContext(),"Товар полностью изъят");
                                    return null;
                                });
                            } else {
                                Memory.replaceFile(Memory.tovarData, Memory.newFile + "\n" +
                                        Memory.dataBaseOtdel.tovars.get(k).barcode + "\n" +
                                        Memory.dataBaseOtdel.tovars.get(k).name_tovar + "\n" +
                                        (Integer.parseInt(task.getResult().split("\n")[3]) - Integer.parseInt(((TextView) findViewById(R.id.kolvo)).getText().toString())) + "\n" +
                                        Memory.Address);
                                Memory.showMessage(getBaseContext(),"Товар Изъят. Остаток: " + (Integer.parseInt(task.getResult().split("\n")[3]) - Integer.parseInt(((TextView) findViewById(R.id.kolvo)).getText().toString())));
                            }
                            finish();
                        } else
                            Memory.showMessage(getBaseContext(),"Невозможно. Остаток товара: " + Integer.parseInt(task.getResult().split("\n")[3]));
                        return null;
                    });
                } catch (ArrayIndexOutOfBoundsException ex) {
                    startActivity(new Intent(getBaseContext(), NotFind.class));
                }
            }
        });
    }
}