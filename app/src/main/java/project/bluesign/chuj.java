package project.bluesign;

import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class chuj extends AppCompatActivity implements Camera.PreviewCallback{

    Camera cameraObj;
    FrameLayout preview;
    private CameraSurfacePreview mPreview;
    private int FRONT_CAMERA_INDEX = 1;
    private int BACK_CAMERA_INDEX = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chuj);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }

    private void startCamera() {
        cameraObj = Camera.open(FRONT_CAMERA_INDEX);
        mPreview = new CameraSurfacePreview(chuj.this, cameraObj);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        cameraObj.setPreviewCallback(chuj.this);
    }
}
