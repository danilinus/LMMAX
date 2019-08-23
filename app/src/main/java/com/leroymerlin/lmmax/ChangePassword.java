package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ChangePassword extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        findViewById(R.id.confirm_btn).setOnClickListener(ChangePass);

        findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Function.class));
            }
        });
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Settings.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), Settings.class));
    }

    public View.OnClickListener ChangePass = new View.OnClickListener() {
        public void onClick(View v) {
            Memory.replaceFile("SYSSET", ((EditText)findViewById(R.id.editText)).getText().toString());
            Memory.pass_settings = ((EditText)findViewById(R.id.editText)).getText().toString();
            startActivity(new Intent(getBaseContext(), Settings.class));
        }
    };

}
