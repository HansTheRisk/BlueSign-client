package project.bluesign;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

import java.io.IOException;

public class CameraPreviewActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private FacialProcessing facialProcessing;
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    protected void onCreate(Bundle savedInstanceState, int layout) {
        super.onCreate(savedInstanceState);
        setContentView(layout);

        surfaceView = (SurfaceView) findViewById(R.id.photoPreview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void startPreview() {
        boolean frontCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        if (frontCamera) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            camera.setDisplayOrientation(90);
            Camera.Parameters param;

            param = camera.getParameters();
            param.getSupportedPreviewSizes().get(1);
            surfaceView.getHolder().setFixedSize(500, 500);

            camera.setParameters(param);

            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "Camera Active!", Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        try {
            camera.stopPreview();
        }
        catch (Exception e) {
        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (Exception e) {
        }
    }

    public FacialProcessing getFacialProcessing() {
        facialProcessing = (FacialProcessing) FacialProcessing.getInstance();
        facialProcessing.setRecognitionConfidence(58);
        facialProcessing.setProcessingMode(FacialProcessing.FP_MODES.FP_MODE_STILL);
        return facialProcessing;
    }

    public SurfaceHolder getSurfaceHolder() {
        return surfaceHolder;
    }

    public Camera getCamera() {
        return camera;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }
}
