package project.bluesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class ResetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);
    }

    public void resetSettings(View view) {
        EditText pin = ((EditText)findViewById(R.id.txtPinReset));
        pin.setError(null);
        if (TextUtils.isEmpty(pin.getText().toString()))
            pin.setError("Don't forget about the pin!");
        else {
            getSharedPreferences("UserInfo", 0).edit().clear().commit(); //Verify user pin here!
        }
    }
}
