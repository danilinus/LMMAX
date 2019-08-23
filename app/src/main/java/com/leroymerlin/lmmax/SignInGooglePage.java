package com.leroymerlin.lmmax;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class SignInGooglePage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_google_page);

        Memory.mGoogleSignInClient = GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestScopes(Drive.SCOPE_FILE).build());
        startActivityForResult(Memory.mGoogleSignInClient.getSignInIntent(), 1);

        findViewById(R.id.log_in_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (Memory.mGoogleSignInClient == null || Memory.mDriveClient == null || Memory.mDriveResourceClient == null) {
                    startActivityForResult(Memory.mGoogleSignInClient.getSignInIntent(), 1);
                } else
                    startActivity(new Intent(getBaseContext(), WriteCity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent ReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, ReturnedIntent);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK)
                    updateViewWithGoogleSignInAccountTask(Memory.mGoogleSignInClient.silentSignIn());
                break;
        }
    }

    private void updateViewWithGoogleSignInAccountTask(Task<GoogleSignInAccount> task) {
        Log.i("Input", "Update view with sign in account task");
        task.addOnSuccessListener(
                new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                        showMessage("Успешный вход");
                        Memory.mDriveClient = Drive.getDriveClient(getApplicationContext(), googleSignInAccount);
                        Memory.mDriveResourceClient = Drive.getDriveResourceClient(getApplicationContext(), googleSignInAccount);
                        Memory.getTextFromFile("SYSSET").continueWithTask(task -> {
                            Memory.pass_settings = task.getResult();
                            return null;
                        });
                        startActivity(new Intent(getBaseContext(), WriteCity.class));
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showMessage("Вход в google аккаунт не выполнен, попробуйте перезайти");
                            }
                        });
    }


    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
