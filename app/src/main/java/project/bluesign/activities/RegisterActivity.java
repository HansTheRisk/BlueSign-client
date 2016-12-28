package project.bluesign.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

import project.bluesign.R;
import project.bluesign.service.settings.SettingsService;

public class RegisterActivity extends AppCompatActivity {

    private SettingsService settingsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        settingsService = new SettingsService(getApplicationContext());
    }

    public void accept(View view) {
        EditText id = ((EditText)findViewById(R.id.txtId));
        EditText pin = ((EditText)findViewById(R.id.txtPin));

        id.setError(null);
        pin.setError(null);

        if (TextUtils.isEmpty(id.getText().toString()))
            id.setError("ID cannot be empty");
        else if (TextUtils.isEmpty(pin.getText().toString()))
            pin.setError("PIN cannot be empty");
        else if(verify(id.getText().toString(), pin.getText().toString())) {

            saveId(id.getText().toString());
            savePin(pin.getText().toString());

            if (FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_PROCESSING)) {
                startActivity(new Intent(this, RegisterFaceActivity.class));
                finish();
            }
            else {
                settingsService.facialRecognitionEnabled(false);
                settingsService.registrationComplete(true);
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
        else {
            Toast.makeText(this, "ID or PIN not recognised!", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean verify(String id, String pin) {
        // TODO Placeholder for the API call
        return true;
    }

    private boolean saveId(String id) {
        return settingsService.saveId(id);
    }

    private boolean savePin(String pin) {
        return settingsService.savePin(pin);
    }
}
