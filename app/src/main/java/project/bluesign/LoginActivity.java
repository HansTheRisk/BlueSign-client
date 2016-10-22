package project.bluesign;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements SurfaceHolder.Callback{

    private FacialProcessing facialProcessing;
    private Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
//            param.setPreviewSize(param.getSupportedPreviewSizes().get(1).width, param.getSupportedPreviewSizes().get(1).height);
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
