package project.bluesign.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

import project.bluesign.R;
import project.bluesign.service.settings.SettingsService;

public class SettingsActivity extends AppCompatActivity {

    private SettingsService settingsService;
    private Switch facialRecognitionSwitch;
    private Button faceUpdateButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_settings);
        context = getApplicationContext();
        settingsService = new SettingsService(context);
        facialRecognitionSwitch = (Switch)findViewById(R.id.faceSwitch);
        faceUpdateButton = (Button)findViewById(R.id.btnUpdateFace);
        initialise();
    }

    @Override
    public void onBackPressed() {

        if ((settingsService.isFacialRecognitionEnabled() && settingsService.loadAlbum() == null) || (facialRecognitionSwitch.isChecked() && settingsService.loadAlbum() == null)) {
            AlertDialog.Builder backAlert  = new AlertDialog.Builder(this);
            backAlert.setMessage("You have enabled facial recognition login, but your face album seems to be empty.\n\nWould you like to complete face registration process?.");
            backAlert.setTitle("Facial recognition enabled");
            backAlert.setCancelable(false);
            backAlert.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(context, RegisterFaceActivity.class));
                            finish();
                        }
                    });
            backAlert.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            settingsService.facialRecognitionEnabled(false);
                            startActivity(new Intent(context, MainActivity.class));
                            finish();
                        }
                    });
            backAlert.create().show();
        }
        else{
            startActivity(new Intent(context, MainActivity.class));
            finish();
        }
    }

    private void initialise() {

        facialRecognitionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchChanged(isChecked);
            }
        });

        if (FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_PROCESSING)) {
            facialRecognitionSwitch.setChecked(settingsService.isFacialRecognitionEnabled());
            faceUpdateButton.setEnabled(facialRecognitionSwitch.isChecked());
        }
        else {
            facialRecognitionSwitch.setChecked(false);
            facialRecognitionSwitch.setEnabled(false);
            faceUpdateButton.setEnabled(false);
        }
    }

    public void updateFace(View view) {
        startActivity(new Intent(this, RegisterFaceActivity.class));
    }

    private void switchChanged(boolean value) {
        settingsService.facialRecognitionEnabled(value);
        faceUpdateButton.setEnabled(value);
    }

}
