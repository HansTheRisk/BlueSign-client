package project.bluesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import project.bluesign.service.settings.SettingsService;

public class ResetActivity extends AppCompatActivity {

    private SettingsService settingsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
        settingsService = new SettingsService(getApplicationContext());
    }

    public void resetSettings(View view) {
        EditText pin = ((EditText)findViewById(R.id.txtPinReset));
        pin.setError(null);
        if (TextUtils.isEmpty(pin.getText().toString()))
            pin.setError("Don't forget about the pin!");
        else {
            if(settingsService.getPin().equals(pin.getText().toString()))
            {
                if (settingsService.resetSettings()) {
                    Toast.makeText(this, "Settings reset", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            else {
                pin.selectAll();
                Toast.makeText(this, "Invalid PIN", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
