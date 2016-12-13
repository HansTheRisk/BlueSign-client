package project.bluesign;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.qualcomm.snapdragon.sdk.face.FaceData;
import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumSet;

import static android.widget.Toast.LENGTH_SHORT;

public class RegisterFaceActivity extends CameraPreviewActivity {

    private FacialProcessing processor;

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


            // Use this image data for implementing the Facial Recognition features.

            testPicture(data);

        }
    };

    public void testPicture(byte[] data) {
        Bitmap storedBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, null);
        processor.setBitmap(storedBitmap);
        processor.getFaceData();
        Toast.makeText(this, Integer.toString(processor.getNumFaces()), Toast.LENGTH_SHORT).show();
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
        if(processor.getFaceData() == null) {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
        }
        else if (processor.getFaceData() != null) {
            Toast.makeText(this, "not null", Toast.LENGTH_SHORT).show();
            processor.addPerson(0);
            Toast.makeText(this, Integer.toString(processor.getNumFaces()), Toast.LENGTH_SHORT).show();
            int x = 1;
            int y = 2;
            x = y;
        }


//        processor.setFrame(data, previewSize.width, previewSize.height, true, FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_90);
//        Toast.makeText(this, processor.getNumFaces(), Toast.LENGTH_SHORT).show();
//        processor.
////        processor.addPerson(1);
//        FaceData[] faceArray = processor.getFaceData();
//        int index = faceArray.length;
//        if (processor.getFaceData() == null)
//            Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(this, processor.getFaceData().length, Toast.LENGTH_SHORT).show();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_register_face);
        boolean isSupported = FacialProcessing.isFeatureSupported(FacialProcessing.FEATURE_LIST.FEATURE_FACIAL_PROCESSING);
        Toast.makeText(this, String.valueOf(isSupported), LENGTH_SHORT).show();
        processor = this.getFacialProcessing();
    }

    public void register(View view) {
        getCamera().takePicture(shutterCallback, rawCallback, registerCallback);
    }

    public void test(View view) {
        getCamera().takePicture(shutterCallback, rawCallback, testCallback);
    }
}
