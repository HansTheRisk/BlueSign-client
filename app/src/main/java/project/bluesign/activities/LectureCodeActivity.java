package project.bluesign.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import project.bluesign.R;

public class LectureCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_code);
    }

    public void verifyCode(View view) {
        //TODO api call
    }
}
