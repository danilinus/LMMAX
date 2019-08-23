package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ContentData extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_data);

        Memory.getTextFromFile(Memory.tovarData.getDriveId().asDriveFile()).continueWithTask(task -> {
            String[] content = task.getResult().split("\n");
            tovarName = "LC" + content[0] + "OT" + Memory.otdel;
            ((TextView) findViewById(R.id.lmcode_lbl)).setText("LMкод: " + content[0]);
            ((TextView) findViewById(R.id.barcode_lbl)).setText("Штрихкод: " + content[1]);
            Memory.newFile = content[0];
            ((TextView) findViewById(R.id.tovar_name_lbl)).setText(content[2]);
            ((TextView) findViewById(R.id.sum_lbl)).setText("Кол-во: " + content[3]);
            ((TextView) findViewById(R.id.address_lbl)).setText("Адрес: " + content[4]);
            Memory.Address = content[4];
            findViewById(R.id.edit_btn).setOnClickListener(EditBtn);
            findViewById(R.id.remove_btn).setOnClickListener(RemoveBtn);
            findViewById(R.id.cancel_btn).setOnClickListener(CancelBtn);
            return null;
        });
    }

    public String tovarName = "";

    private View.OnClickListener EditBtn = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(), Resize.class));
            finish();
        }
    };

    private View.OnClickListener RemoveBtn = new View.OnClickListener() {
        public void onClick(View v) {
            //Memory.tovarsBaseOtdel.get(Memory.Address).RemoveTovar(tovarName, Memory.tovarData);
            finish();
        }
    };
    private View.OnClickListener CancelBtn = new View.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };
}