package com.leroymerlin.lmmax;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.leroymerlin.lmmax.Memory.otdel;

public class Input extends Activity {

    private GridLayout gridButtons;
    AlertDialog.Builder dlgAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gridButtons = findViewById(R.id.grid_buttons);


        for (int i = 0; i < gridButtons.getChildCount(); i++) {
            Button b = findViewById(gridButtons.getChildAt(i).getId());
            b.setTag(i + 1);
            b.setOnClickListener(GridButtonsClick);
        }

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dlgAlert = new AlertDialog.Builder(this, R.style.LightDialogTheme);
        dlgAlert.setPositiveButton("ОБЗОР",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Memory.formats = ".xltx .xslx";
                        startActivityForResult(new Intent(getBaseContext(), FileManager.class), 2);
                    }
                });
        dlgAlert.setMessage("Для работы приложения необходимо указать гамму товара выбранного отдела (выгруженную из Pyxis на ПК). Сохраните гамму в формате .xltx на телефон и укажите к ней путь.");
        dlgAlert.setTitle("LM MAX");

        findViewById(R.id.settings_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), InputPass.class));
            }
        });
    }

    public void onResume(){
        super.onResume();
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawableResource(R.color.main_color);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent ReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, ReturnedIntent);
        if (resultCode == RESULT_OK) {
            try {
                Memory.dataBaseOtdel = Memory.ExcelToBase(new FileInputStream(new File(ReturnedIntent.getStringExtra("name"))));
                Memory.createFile("XLTOTD" + otdel, Memory.dataBaseOtdel);
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this, R.style.LightDialogTheme);
                dlgAlert.setPositiveButton("Я ПОНЯЛ!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getBaseContext(), Function.class));
                            }
                        });
                dlgAlert.setMessage("Каждый адрес Вы создаёте самостоятельно в настройках приложения.\n" +
                        "Для удобства ориентировки в отделе необходимо загрузить карту отдела (индивидуально созданную в любом редакторе изображений на ПК) в формате изображения.");
                dlgAlert.setTitle("LM MAX");
                dlgAlert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        startActivity(new Intent(getBaseContext(), LoadingExcel.class));
                    }
                });
                dlgAlert.create().show();
            } catch (FileNotFoundException e) {
                showMessage("Произошла ошибка");
            }
        }
    }

    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener GridButtonsClick = new View.OnClickListener() {
        public void onClick(View v) {
            Memory.getMetadataBuffer("infsys" + Memory.city).continueWith(task ->
            {
                if (task.getResult().getCount() > 0) {
                    Memory.getTextFromFile("infsys" + Memory.city).continueWith(task1 -> {
                        String[] g = task1.getResult().replace("_\n", "").split("_");
                        int i;
                        String s = "";
                        for (i = 0; i < g.length; i += 3)
                            if (g[i].contains(v.getTag() + ""))
                                break;
                        if (i >= g.length - 1)
                            s = task1.getResult().replace("\n", "") + v.getTag() + "_1_" + new SimpleDateFormat("HH:mm dd.MM.yyyy").format(new Date()) + "_";
                        else {
                            g[i + 1] = (Integer.parseInt(g[i + 1]) + 1) + "";
                            g[i + 2] = new SimpleDateFormat("HH:mm dd.MM.yyyy").format(new Date());
                            for (i = 0; i < g.length; i++) s += g[i] + '_';
                        }
                        Memory.replaceFile("infsys" + Memory.city, s);
                        return null;
                    });
                } else
                    Memory.createFile("infsys" + Memory.city, v.getTag() + "_1_" + new SimpleDateFormat("HH:mm dd.MM.yyyy").format(new Date()) + "_");
                return null;
            });
            otdel = Memory.city + v.getTag();
            Memory.getMetadataBuffer("XLTOTD" + Memory.otdel).continueWithTask(task -> {
                if (task.getResult().getCount() > 0) {
                    //Memory.SyncAddress();
                    startActivity(new Intent(getBaseContext(), LoadingExcel.class));
                } else dlgAlert.create().show();
                return null;
            });

        }
    };
}