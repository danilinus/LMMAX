package com.leroymerlin.lmmax;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class InputDel extends Activity {

    private GridLayout gridButtons;
    AlertDialog.Builder dlgAlert;
    public String otd = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        gridButtons = findViewById(R.id.grid_buttons);

        for (int i = 0; i < gridButtons.getChildCount(); i++) {
            Button b = findViewById(gridButtons.getChildAt(i).getId());
            b.setTag(i + 1);
            b.setOnClickListener(GridButtonsClick);
        }

        ((TextView)findViewById(R.id.sel_otd_lbl)).setTextSize(18);
        ((TextView)findViewById(R.id.sel_otd_lbl)).setText("ВЫБИРИТЕ ОТДЕЛ В КОТОРОМ СЛЕДУЕТ УДАЛИТЬ ГАММУ");

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dlgAlert = new AlertDialog.Builder(this, R.style.LightDialogTheme);
        dlgAlert.setPositiveButton("ДА",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Memory.deleteFile("XLTOTD"+Memory.city + otd);
                        finish();
                    }
                });
        dlgAlert.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dlgAlert.setMessage("Вы уверены, что хотите удалить гамму товара из данного отдела?");
        dlgAlert.setTitle("LM MAX");

        findViewById(R.id.settings_btn).setVisibility(View.GONE);
    }

    private View.OnClickListener GridButtonsClick = new View.OnClickListener() {
        public void onClick(View v) {
            otd = v.getTag().toString();
            dlgAlert.create().show();
        }
    };
}
