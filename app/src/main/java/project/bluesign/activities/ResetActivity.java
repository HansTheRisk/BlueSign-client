package project.bluesign.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import project.bluesign.R;
import project.bluesign.service.settings.SettingsService;

public class ResetActivity extends AppCompatActivity {

    private SettingsService settingsService;
    private EditText pin;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_reset);
        settingsService = new SettingsService(getApplicationContext());
        pin = ((EditText)findViewById(R.id.txtPinReset));
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        pin.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void resetSettings(View view) {
        pin.setError(null);
        if (TextUtils.isEmpty(pin.getText().toString())) {
            pin.setError("Don't forget about the pin!");
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
        else {
            if(settingsService.isPinCorrect(pin.getText().toString()))
            {
                if (settingsService.resetSettings()) {
                    Toast.makeText(this, "Settings reset", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    imm.hideSoftInputFromWindow(pin.getWindowToken(), 0);
                    finish();
                }
            }
            else {
                pin.selectAll();
                Toast.makeText(this, "Invalid PIN", Toast.LENGTH_SHORT).show();
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }
    }
}
