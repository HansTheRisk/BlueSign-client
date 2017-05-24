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

import project.bluesign.R;
import project.bluesign.service.settings.SettingsService;

public class PinLoginActivity extends AppCompatActivity {

    private Intent nextActivity;
    private SettingsService settingsService;
    private EditText pin;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_pin_login);
        settingsService = new SettingsService(getApplicationContext());
        pin = ((EditText)findViewById(R.id.txtPin));
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        if (getIntent().getExtras() != null)
            nextActivity = (Intent)getIntent().getExtras().get("intent");

        pin.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void checkPin(View view) {
        EditText pin = ((EditText)findViewById(R.id.txtPin));
        if (TextUtils.isEmpty(pin.getText().toString()))
            pin.setError("Don't forget about the pin!");
        else {
            if(settingsService.isPinCorrect(pin.getText().toString()))
            {
                if (nextActivity != null) {
                    imm.hideSoftInputFromWindow(pin.getWindowToken(), 0);
                    finish();
                    startActivity(nextActivity);
                }
            }
            else {
                pin.selectAll();
                pin.setError("Invalid pin");
            }
        }
    }
}
