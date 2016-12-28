package project.bluesign.activities;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import java.io.IOException;

import project.bluesign.R;

public class CameraPreviewActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
//    private RadioButton detectionCheckbox;

    protected void onCreate(Bundle savedInstanceState, int layout) {
        super.onCreate(savedInstanceState);
        setContentView(layout);

        surfaceView = (SurfaceView) findViewById(R.id.photoPreview);
//        detectionCheckbox = (RadioButton) findViewById(R.id.detected);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void startPreview() {
        boolean frontCamera = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

        if (frontCamera) {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            camera.setDisplayOrientation(90);

//            camera.setPreviewCallback(new Camera.PreviewCallback() {
//
//                @Override
//                public void onPreviewFrame(byte[] data, Camera camera) {
//                    final byte[] imageData = data;
//                    final Camera threadCamera = camera;
//
//                    final Runnable runnabe = new Runnable(){
//                        public void run() {
//                            Camera.Parameters parameters = threadCamera.getParameters();
//                            Camera.Size size = parameters.getPreviewSize();
//
//                            FacialProcessing processor = getFacialProcessing();
//                            processor.setFrame(imageData, size.width, size.height, true, FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_90);
//                            if(processor.getNumFaces() == 1) {
//                                detectionCheckbox.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        detectionCheckbox.setChecked(true);
//                                    }
//                                });
//                            }
//                            else {
//                                detectionCheckbox.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        detectionCheckbox.setChecked(false);
//                                    }
//                                });
//                            }
//                            processor.release();
//                        }
//                    };
//                    runnabe.run();
//                }
//            });

            surfaceView.getHolder().setFixedSize(800, 800);

            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        FacialProcessing facialProcessing = FacialProcessing.getInstance();
        facialProcessing.setRecognitionConfidence(58);
        facialProcessing.setProcessingMode(FacialProcessing.FP_MODES.FP_MODE_STILL);
        return facialProcessing;
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
