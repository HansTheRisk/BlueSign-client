package project.bluesign.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import project.bluesign.R;
import project.bluesign.service.settings.SettingsService;
/**
 * This activity provides face login functionality.
 */
public class FaceLoginActivity extends CameraPreviewActivity {

    private SettingsService settingsService;
    private int failedAttempts = 0;

    /**
     * This method initialises the activity
     * @param savedInstanceState
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_face_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

    /**
     * This method performs the comparison of the captured face image
     * to the images in the album
     * @param data
     */
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
                    problemWithFaces();
                }
                else {
                    Toast.makeText(this, "Face recognised!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LectureCodeActivity.class));
                }
            }
            else {
                Toast.makeText(this, "More than one face detected!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "No faces detected!", Toast.LENGTH_SHORT).show();
            problemWithFaces();
        }
        processor.release();
    }

    /**
     * This method counts the unsuccesfull face authentications
     * and asks the user if he/she wants to use PIN instead
     */
    private void problemWithFaces() {
        failedAttempts++;
        if (failedAttempts > 3) {
            final Context context = getApplicationContext();
            final AlertDialog.Builder alternateLogin  = new AlertDialog.Builder(this);
            alternateLogin.setMessage("Would you like to try to log in with your Security PIN?");
            alternateLogin.setTitle("Alternative login");
            alternateLogin.setCancelable(false);
            alternateLogin.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(context, PinLoginActivity.class)
                                    .putExtra("intent", new Intent(context, LectureCodeActivity.class)));
                            finish();
                        }
                    });
            alternateLogin.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alternateLogin.create().show();
        }
    }

    /**
     * Event handler for the sign in button
     */
    public void signIn(View view) {
        getCamera().takePicture(shutterCallback, rawCallback, loginCallback);
    }
}
