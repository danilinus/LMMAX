package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddressSelected extends Activity {

    LinearLayout Arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_selected);

        findViewById(R.id.home_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Function.class));
            }
        });
        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), AddressStorage.class));
            }
        });

        Arr = findViewById(R.id.array_buttons);

        Memory.UpdateTovarBase().continueWithTask(task ->
        {
            String[] adress = Memory.tovarsBaseOtdel.get().split(" ");
            for (int i = 0; i < adress.length; i++) {
                Button b = new Button(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 10, 10, 10);
                b.setLayoutParams(params);
                b.setText(adress[i]);
                b.setOnClickListener(SelectedBtn);
                Arr.addView(b);
            }return null;
        });

        ((EditText)findViewById(R.id.find_text)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                changetText(s.toString());
            }
        });
    }

    public void changetText(String s)
    {
        Arr.removeAllViews();
        String[] adress = Memory.tovarsBaseOtdel.get().split(" ");
        for (int i = 0; i < adress.length; i++) {
            Button b = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 10, 10, 10);
            b.setLayoutParams(params);
            b.setText(adress[i]);
            b.setOnClickListener(SelectedBtn);
            Arr.addView(b);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), AddressStorage.class));
    }

    private View.OnClickListener SelectedBtn = new View.OnClickListener() {
        public void onClick(View v) {
            Memory.Address = ((Button)v).getText().toString();
            startActivity(new Intent(getBaseContext(), SelectCreate.class));
        }
    };
}
