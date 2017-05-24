package project.bluesign.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import project.bluesign.R;
import project.bluesign.domain.accessCode.AccessCode;
import project.bluesign.domain.message.Message;
import project.bluesign.service.attendance.AttendanceService;
import project.bluesign.service.settings.SettingsService;

import static project.bluesign.constant.GlobalVariables.STUDENT_ENDPOINT;

public class LectureCodeActivity extends AppCompatActivity {

    private SettingsService settingsService;
    private AttendanceService attendanceService;
    private EditText code;
    private TextView info;
    private Button accept;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsService = new SettingsService(getApplicationContext());
        attendanceService = new AttendanceService();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_lecture_code);

        code = ((EditText)findViewById(R.id.txtCode));
        info = ((TextView)findViewById(R.id.lblInfo));
        accept = ((Button)findViewById(R.id.btnAccept));

        code.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void verifyCode(View view) {
        if (TextUtils.isEmpty(code.getText().toString()))
            code.setError("Don't forget about the code!");
        else {
            imm.hideSoftInputFromWindow(code.getWindowToken(), 0);
            attendanceService.signIn(this, settingsService.getId(), settingsService.getPin());
        }
    }

    public EditText getCode() {
        return code;
    }

    public TextView getInfo() {
        return info;
    }

    public Button getAccept() {
        return accept;
    }

    public InputMethodManager getImm() {
        return imm;
    }
}
