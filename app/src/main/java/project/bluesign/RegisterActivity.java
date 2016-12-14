package project.bluesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void accept(View view) {
        EditText id = ((EditText)findViewById(R.id.txtBoxId));
        EditText pin = ((EditText)findViewById(R.id.txtPin));

        id.setError(null);
        pin.setError(null);

        if (TextUtils.isEmpty(id.getText().toString()))
            id.setError("ID cannot be empty");
        else if (TextUtils.isEmpty(pin.getText().toString()))
            pin.setError("PIN cannot be empty");
        else if(saveId(id.getText().toString()) && savePin(pin.getText().toString())) { //Insert ID and PIN check / API call
            if (FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_PROCESSING)) {
                startActivity(new Intent(this, RegisterFaceActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }

    }

    private boolean verify() {
        // Placeholder for the API call
        return true;
    }

    private boolean saveId(String id) {
        return getSharedPreferences("UserInfo", 0).edit().putString("id", id).commit();
    }

    private boolean savePin(String pin) {
        return getSharedPreferences("UserInfo", 0).edit().putString("pin", pin).commit();
    }
}
