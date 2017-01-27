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
    private final String ATTENDANCE_SIGN_IN = "signIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsService = new SettingsService(getApplicationContext());
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
            accept.setEnabled(false);
            info.setTextColor(Color.YELLOW);
            info.setText("Please wait...");
            new SignInExecutor(info).execute(settingsService.getId(), settingsService.getPin(), code.getText().toString());
        }
    }

    private class SignInExecutor extends AsyncTask<String, Void, Boolean> {

        private Message message = new Message();
        private TextView tv;
        SignInExecutor(TextView tv) {
            this.tv = tv;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                final String url = STUDENT_ENDPOINT + "/" + params[0] + "/" + ATTENDANCE_SIGN_IN + "/" + params[1];
                HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
                factory.setConnectTimeout(5000);
                RestTemplate restTemplate = new RestTemplate(factory);
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                ResponseEntity responseEntity = restTemplate.postForEntity(url, new AccessCode(Integer.valueOf(params[2])), null);
                JSONObject object = new JSONObject((String)responseEntity.getBody());
                message = new Message(object.getString("message"), String.valueOf(responseEntity.getStatusCode().value()));

            } catch (HttpClientErrorException e) {
                try {
                    JSONObject object = new JSONObject(e.getResponseBodyAsString());
                    message = new Message(object.getString("message"), String.valueOf(e.getStatusCode().value()));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean res) {
            tv.setText("TEST!");
        }
    }
}
