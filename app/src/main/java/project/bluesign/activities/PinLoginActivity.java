package project.bluesign.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import project.bluesign.R;
import project.bluesign.service.settings.SettingsService;

public class PinLoginActivity extends AppCompatActivity {

    private Intent nextActivity;
    private SettingsService settingsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_login);
        settingsService = new SettingsService(getApplicationContext());
        if (getIntent().getExtras() != null)
            nextActivity = (Intent)getIntent().getExtras().get("intent");
    }

    public void checkPin(View view) {
        EditText pin = ((EditText)findViewById(R.id.txtPin));
        if (TextUtils.isEmpty(pin.getText().toString()))
            pin.setError("Don't forget about the pin!");
        else {
            if(settingsService.isPinCorrect(pin.getText().toString()))
            {
                if (nextActivity != null) {
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
