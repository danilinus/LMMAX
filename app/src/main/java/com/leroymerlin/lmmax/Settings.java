package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.leroymerlin.lmmax.Memory.otdel;

public class Settings extends Activity {

    private final int Pick_image = 1;
    private final int Pick_excel = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViewById(R.id.address_list_btn).setOnClickListener(AddressListBtn);
        findViewById(R.id.change_map).setOnClickListener(ChangeMap);
        findViewById(R.id.change_pass_btn).setOnClickListener(ChangePass);
        findViewById(R.id.change_excel_btn).setOnClickListener(ChangeExcel);
        findViewById(R.id.excel_otch_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), ExportExcel.class));
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
        findViewById(R.id.statistic_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), InfoCities.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), Function.class));
    }

    private View.OnClickListener AddressListBtn = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(), AddressList.class));
        }
    };

    public View.OnClickListener ChangeMap = new View.OnClickListener() {
        public void onClick(View v) {
            Memory.formats = ".png .jpg .jpeg .gif";
            startActivityForResult(new Intent(getBaseContext(), FileManager.class), Pick_image);
        }
    };

    public View.OnClickListener ChangeExcel = new View.OnClickListener() {
        public void onClick(View v) {
            Memory.formats = ".xltx .xslx";
            startActivityForResult(new Intent(getBaseContext(), FileManager.class), Pick_excel);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent ReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, ReturnedIntent);

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    try {
                        Memory.replaceFile("MAPOTD" + otdel, new FileInputStream(new File(ReturnedIntent.getStringExtra("name"))));
                        //Memory.replaceFile("MAPOTD"+otdel, getContentResolver().openInputStream(ReturnedIntent.getData()));
                    } catch (FileNotFoundException e) {
                        showMessage("Произошла ошибка");
                    }
                }
                break;
            case Pick_excel:
                if (resultCode == RESULT_OK) {
                    try {
                        Memory.replaceFile("XLTOTD" + otdel, new FileInputStream(new File(ReturnedIntent.getStringExtra("name"))));
                        startActivity(new Intent(getBaseContext(), LoadingExcel.class));
                    } catch (FileNotFoundException e) {
                        showMessage("Произошла ошибка");
                        Log.e("S", e.toString());
                    }
                }
                break;
        }
    }

    public View.OnClickListener ChangePass = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(), ChangePassword.class));
        }
    };

    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
