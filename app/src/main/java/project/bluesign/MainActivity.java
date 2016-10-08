package project.bluesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

public class MainActivity extends AppCompatActivity {

    public static FacialProcessing facialProcessing;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
