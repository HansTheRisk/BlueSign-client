package project.bluesign;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.qualcomm.snapdragon.sdk.face.FaceData;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import com.qualcomm.snapdragon.sdk.face.FacialProcessingConstants;

import project.bluesign.service.settings.SettingsService;

public class LoginActivity extends CameraPreviewActivity {

    private SettingsService settingsService;
    private int failedAttempts = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_login);
        settingsService = new SettingsService(this.getApplicationContext());
    }

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            Log.d("TAG", "onShutter'd");
        }
    };


    Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.d("TAG", "onPictureTaken - raw");
        }
    };

    Camera.PictureCallback loginCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            compare(data);
        }
    };

    public void compare(byte[] data) {
        Bitmap storedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, null);
        FacialProcessing processor = this.getFacialProcessing();
        processor.deserializeRecognitionAlbum(settingsService.loadAlbum());
        processor.setBitmap(storedBitmap);

        if(processor.getFaceData() != null) {
            if(processor.getFaceData().length == 1) {
                FaceData[] faceData = processor.getFaceData();
                int faceId = faceData[0].getPersonId();
                if (faceId == FacialProcessingConstants.FP_PERSON_NOT_REGISTERED) {
                    Toast.makeText(this, "Face not recognised!", Toast.LENGTH_SHORT).show();
                    failedAttempts++;

                    if (failedAttempts > 3) {
                        // Alternative login
                    }
                }
                else {
                    Toast.makeText(this, "Face recognised!", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "More than one face detected!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "No faces detected!", Toast.LENGTH_SHORT).show();
        }

        processor.release();
    }

    public void signIn(View view) {
        getCamera().takePicture(shutterCallback, rawCallback, loginCallback);
    }
}
