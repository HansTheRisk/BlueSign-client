package project.bluesign;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

import project.bluesign.service.settings.SettingsService;

public class MainActivity extends AppCompatActivity {

    private SettingsService settingsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        settingsService = new SettingsService(getApplicationContext());
        checkSettings();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setContentView(R.layout.activity_main);
        checkSettings();
    }

    private void checkSettings() {
        if (settingsService.isRegistrationComplete()) {
            (findViewById(R.id.btnRegister)).setEnabled(false);
        }
        else {
            (findViewById(R.id.btnLogin)).setEnabled(false);
            (findViewById(R.id.lblReset)).setEnabled(false);
        }
    }

    public void login(View view) {
        if (FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_PROCESSING) && settingsService.isFacialRecognitionEnabled()) {
            startActivity(new Intent(this, FaceLoginActivity.class));
        }
        else {
            //start PIN login activity
        }
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void reset(View view) {
        startActivity(new Intent(this, ResetActivity.class));
        finish();
    }
}
