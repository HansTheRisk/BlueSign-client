package project.bluesign.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import project.bluesign.R;
import project.bluesign.domain.binary.BinaryObject;
import project.bluesign.service.HttpsClient;
import project.bluesign.service.settings.SettingsService;

import static project.bluesign.constant.GlobalVariables.STUDENT_ENDPOINT;

public class RegisterActivity extends AppCompatActivity {

    private SettingsService settingsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);
        settingsService = new SettingsService(getApplicationContext());
    }

    public void accept(View view) {
        EditText id = ((EditText)findViewById(R.id.txtId));
        EditText pin = ((EditText)findViewById(R.id.txtPin));

        id.setError(null);
        pin.setError(null);

        if (TextUtils.isEmpty(id.getText().toString()))
            id.setError("ID cannot be empty");
        else if (TextUtils.isEmpty(pin.getText().toString()))
            pin.setError("PIN cannot be empty");
        else
            new IdAndPinCheckerTwo(id.getText().toString(), pin.getText().toString()).execute();
    }

    private boolean saveId(String id) {
        return settingsService.saveId(id);
    }

    private boolean savePin(String pin) {
        return settingsService.savePin(pin);
    }

    private class IdAndPinCheckerTwo extends AsyncTask<String, Void, BinaryObject> {

        private ProgressDialog pd;
        private String id;
        private String pin;

        public IdAndPinCheckerTwo(String id, String pin) {
            this.id = id;
            this.pin = pin;
        }

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(RegisterActivity.this,"","Please Wait",false);
        }

        @Override
        protected BinaryObject doInBackground(String... params) {
            try {
                HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                RestTemplate restTemplate = new RestTemplate(requestFactory);
                restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpsClient.getNewHttpClient()));
                final String url = STUDENT_ENDPOINT + "/" + id + "/" + pin;
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                BinaryObject binaryObject = restTemplate.getForObject(url, BinaryObject.class);
                return binaryObject;
            } catch (Exception e) {
                return new BinaryObject();
            }
        }

        @Override
        protected void onPostExecute(BinaryObject binaryObject) {
            pd.dismiss();
            if(!binaryObject.getValue())
                Toast.makeText(RegisterActivity.this, "ID or PIN not recognised!", Toast.LENGTH_SHORT).show();
            else {
                saveId(id);
                savePin(pin);

                if (FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_PROCESSING)) {
                    finish();
                    startActivity(new Intent(RegisterActivity.this, RegisterFaceActivity.class));
                } else {
                    finish();
                    settingsService.facialRecognitionEnabled(false);
                    settingsService.registrationComplete(true);
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            }
        }
    }

}
