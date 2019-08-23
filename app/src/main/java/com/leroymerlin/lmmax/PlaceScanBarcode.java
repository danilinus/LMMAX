package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class PlaceScanBarcode extends Activity {


    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        scannerView = new ZXingScannerView((findViewById(R.id.camera_col)).getContext());
        ((RelativeLayout) findViewById(R.id.camera_col)).addView(scannerView);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ((ToggleButton) findViewById(R.id.light_chk)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                scannerView.setFlash(isChecked);
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerView.findFocus();
            }
        });

        setTitle("Адрес: " + Memory.Address);

        if (!checkPermission())
            requestPermission();
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermission()) {
            if (scannerView == null) scannerView = new ZXingScannerView(( findViewById(R.id.camera_col)).getContext());
            scannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
                @Override
                public void handleResult(Result result) {
                    try {
                        Memory.newFile = Memory.dataBaseOtdel.tovars.get(Memory.dataBaseOtdel.findAtBarcode(result.getText())).lmcode;
                        startActivity(new Intent(getBaseContext(), Success.class));
                    } catch (Exception ex) {
                        startActivity(new Intent(getBaseContext(), NotFind.class));
                    }
                }
            });
            scannerView.setFlash(((ToggleButton) findViewById(R.id.light_chk)).isChecked());
            scannerView.startCamera();
        } else requestPermission();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.setFlash(false);
        scannerView.setFlash(false);
        scannerView.stopCamera();
    }
}
