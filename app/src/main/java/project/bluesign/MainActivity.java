package project.bluesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkSettings();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        setContentView(R.layout.activity_main);
        checkSettings();
    }

    private void checkSettings() {
        if (getSharedPreferences("UserInfo", 0).contains("id"))
            (findViewById(R.id.btnRegister)).setEnabled(false);
        else
            (findViewById(R.id.btnLogin)).setEnabled(false);
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void reset(View view) {
        startActivity(new Intent(this, ResetActivity.class));
    }
}
