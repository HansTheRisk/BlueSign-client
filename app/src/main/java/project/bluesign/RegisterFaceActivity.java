package project.bluesign;

import android.content.Intent;
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

public class RegisterFaceActivity extends CameraPreviewActivity {

    private FacialProcessing processor;
    private int faces = 0;

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

//            FileOutputStream stream = null;
//            File mediaStorageDir = new File(Environment.DIRECTORY_PICTURES);
//            try {
//                stream = new FileOutputStream(mediaStorageDir);
//                stream.write(data);
//                stream.close();
//            }
//            catch (FileNotFoundException e) {
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            registerPicture(data);
            // Use this image data for implementing the Facial Recognition features.

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

//        Camera.Parameters params = getCamera().getParameters();
//        Camera.Size previewSize = params.getPreviewSize();
////        ImageView
        Bitmap storedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, null);
//
//        FileOutputStream fos = null;
//        try {
//            fos = openFileOutput("test", Context.MODE_PRIVATE);
//            storedBitmap.compress(Bitmap.CompressFormat.PNG, 85, fos);
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        ImageView myImage = (ImageView) findViewById(R.id.imageView);
//        myImage.setImageURI(Uri.parse(getFilesDir().getPath() + "/test.png"));



        processor.setBitmap(storedBitmap);
        if(processor.getFaceData() != null) {
            if(processor.getFaceData().length == 1) {
                FaceData[] faceData = processor.getFaceData();
                int faceId = faceData[0].getPersonId();
                if (faceId == FacialProcessingConstants.FP_PERSON_NOT_REGISTERED) {
                    processor.addPerson(0);
                }
                else {
                    processor.updatePerson(faceId, 0);
                }
                faces++;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_register_face);
        (findViewById(R.id.btnTest)).setEnabled(false);
        (findViewById(R.id.btnAccept)).setEnabled(false);
        processor = this.getFacialProcessing();
    }

    public void register(View view) {
        getCamera().takePicture(shutterCallback, rawCallback, registerCallback);
    }

    public void test(View view) {
        getCamera().takePicture(shutterCallback, rawCallback, testCallback);
    }

    public void accept(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
