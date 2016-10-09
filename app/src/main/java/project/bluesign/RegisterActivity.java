package project.bluesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

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
        else
            saveId(id.getText().toString());
    }

    private boolean verify() {
        return true;
    }

    private boolean saveId(String id) {
        return getSharedPreferences("UserInfo", 0).edit().putString("id", id).commit();
    }
}
