package project.bluesign;

import android.os.Bundle;
import android.widget.Toast;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

public class RegisterFaceActivity extends CameraPreviewActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_register_face);
        boolean isSupported = FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_PROCESSING);
        Toast.makeText(this, String.valueOf(isSupported), Toast.LENGTH_SHORT).show();
    }
}
