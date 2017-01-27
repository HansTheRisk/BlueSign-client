package project.bluesign.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
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
    private final String ATTENDANCE_SIGN_IN = "signIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsService = new SettingsService(getApplicationContext());
        attendanceService = new AttendanceService();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_lecture_code);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    public void verifyCode(View view) {
        EditText code = ((EditText)findViewById(R.id.txtCode));
        TextView info = ((TextView)findViewById(R.id.lblInfo));
        Button accept = ((Button)findViewById(R.id.btnAccept));

        if (TextUtils.isEmpty(code.getText().toString()))
            code.setError("Don't forget about the code!");
        else {
            attendanceService.signIn(info, accept, settingsService.getId(), settingsService.getPin(), code.getText().toString());
        }
    }
}
