package project.bluesign;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import com.qualcomm.snapdragon.sdk.face.FaceData;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;
import com.qualcomm.snapdragon.sdk.face.FacialProcessingConstants;

import project.bluesign.service.settings.SettingsService;

public class RegisterFaceActivity extends CameraPreviewActivity {

    private FacialProcessing processor;
    private RadioButton firstSample;
    private RadioButton secondSample;
    private RadioButton thirdSample;
    private CheckBox testOK;
    private int faces = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_register_face);
        firstSample = (RadioButton) findViewById(R.id.firstSample);
        secondSample = (RadioButton) findViewById(R.id.secondSample);
        thirdSample = (RadioButton) findViewById(R.id.thirdSample);
        testOK = (CheckBox) findViewById(R.id.test);
        (findViewById(R.id.btnTest)).setEnabled(false);
        (findViewById(R.id.btnAccept)).setEnabled(false);
        processor = this.getFacialProcessing();
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

    Camera.PictureCallback registerCallback = new Camera.PictureCallback() {

        public void onPictureTaken(byte[] data, Camera camera) {
            registerPicture(data);
        }
    };

    Camera.PictureCallback testCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            testPicture(data);
        }
    };

    public void testPicture(byte[] data) {
        Bitmap storedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, null);
        processor.setBitmap(storedBitmap);
        if(processor.getFaceData() != null) {
            if(processor.getFaceData().length == 1) {
                FaceData[] faceData = processor.getFaceData();
                int faceId = faceData[0].getPersonId();
                if (faceId == FacialProcessingConstants.FP_PERSON_NOT_REGISTERED) {
                    Toast.makeText(this, "Face not recognised!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Face recognised!", Toast.LENGTH_SHORT).show();
                    testOK.setChecked(true);
                    (findViewById(R.id.btnAccept)).setEnabled(true);
                }
            }
            else {
                Toast.makeText(this, "More than one face detected!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "No faces detected!", Toast.LENGTH_SHORT).show();
        }
    }

    public void registerPicture(byte[] data){

        Bitmap storedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, null);
        processor.setBitmap(storedBitmap);
        if(processor.getFaceData() != null) {
            if(processor.getNumFaces() == 1) {
                FaceData[] faceData = processor.getFaceData();
                int faceId = faceData[0].getPersonId();
                if (faceId == FacialProcessingConstants.FP_PERSON_NOT_REGISTERED) {
                    processor.addPerson(0);
                }
                else {
                    processor.updatePerson(faceId, 0);
                }
                faces++;
                switch(faces) {
                    case 1:
                        firstSample.setChecked(true);
                        break;
                    case 2:
                        secondSample.setChecked(true);
                        break;
                    case 3:
                        thirdSample.setChecked(true);
                        break;
                }
                Toast.makeText(this, "Photo successfully added!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "More than one face detected!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "No faces detected!", Toast.LENGTH_SHORT).show();
        }
        if (faces == 3)
            (findViewById(R.id.btnTest)).setEnabled(true);
    }

    public void register(View view) {
        getCamera().takePicture(shutterCallback, rawCallback, registerCallback);
    }

    public void test(View view) {
        getCamera().takePicture(shutterCallback, rawCallback, testCallback);
    }

    public void accept(View view) {
        startActivity(new Intent(this, MainActivity.class));
        SettingsService service = new SettingsService(getApplicationContext());
        service.saveAlbum(processor.serializeRecogntionAlbum());
        service.registrationComplete(true);
        processor.release();
        finish();
    }

    @Override
    public void onBackPressed() {
        processor.release();
        finish();
    }
}
