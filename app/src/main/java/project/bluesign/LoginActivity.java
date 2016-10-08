package project.bluesign;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

public class LoginActivity extends AppCompatActivity {

    public static FacialProcessing facialProcessing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        startPreview();
    }

    private void startPreview() {
        boolean frontCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

        
    }
}
