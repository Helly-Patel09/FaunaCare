package com.vahapps.faunacare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadTreatedPic extends AppCompatActivity implements View.OnClickListener{
    private ImageButton buttonChoose;
    private ImageView imageView;
    private DatabaseReference mDatabase;
    private String newPicFile;
    private File outFile;
    private String mCameraFileName;
    final int TAKE_PICTURE = 1;
    private Button buttonUpload;
    private StorageReference storageReference;
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    private String faunaId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_treated_pic);

        buttonChoose = (ImageButton) findViewById(R.id.capture);
        buttonUpload = (Button) findViewById(R.id.upload);
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.ShowImageView);
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        faunaId = getIntent().getStringExtra("faunaKey");
    }

    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        else if (v == buttonUpload) {
            uploadFile();
        }
    }

    private void uploadFile() {

        if (Uri.fromFile(outFile) != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.setCancelable(false);
            progressDialog.show();
            final String currentDate=DateFormat.getDateInstance().format(new Date());
            final String currentTime=DateFormat.getTimeInstance().format(new Date());
           /* imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache(true);
            File imageFile = outFile;
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(imageFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap.createScaledBitmap(imageView.getDrawingCache(true), outputSize,
                    outputSize, false).compress(Bitmap.CompressFormat.JPEG, 100,
                    fileOutputStream);
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setDrawingCacheEnabled(false);*/
            //getting the storage reference
            StorageReference sRef = storageReference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + ".jpg" );

            //adding the file to reference
            sRef.putFile(Uri.fromFile(outFile))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            mDatabase.child("Fauna").child(faunaId).child("treatedUrl").setValue(taskSnapshot.getDownloadUrl().toString());
                            mDatabase.child("Fauna").child(faunaId).child("status").setValue("6");

                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            Intent i=new Intent(UploadTreatedPic.this,ChooseVol.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("faunaKey",faunaId);
                            startActivity(i);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }

    private void showFileChooser() {
        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*/
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PICTURE);*/
        /*String path = Environment.getExternalStorageDirectory() + "/CameraImages/example.jpg";
        File file = new File(path);
        Uri outputFileUri = Uri.fromFile( file );
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
        intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );
        startActivityForResult( intent, TAKE_PICTURE );*/
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("-mm-ss");

        File direct = new File(Environment.getExternalStorageDirectory() +"/faunapics");
        if (!direct.exists()) {
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() +"/faunapics/");
            wallpaperDirectory.mkdirs();
        }
        newPicFile = "Fauna"+ df.format(date) + ".jpg";
        //String outPath = Environment.getExternalStorageDirectory() +"/pics/"+ newPicFile;
        //Log.d("outpathhhh",outPath);
        //File outFile = new File(outPath);
        outFile = new File(Environment.getExternalStorageDirectory() +"/faunapics/", newPicFile);
        Log.d("newoutpathhhh",outFile.getPath());
        mCameraFileName = outFile.toString();
        Uri outuri = Uri.fromFile(outFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        startActivityForResult(intent, TAKE_PICTURE);
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            mCapturedImageURI = getContentResolver()
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values);
            takePictureIntent
                    .putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
            startActivityForResult(takePictureIntent, TAKE_PICTURE);
        }*/
        /*File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
            Log.d("mylog", "Exception while creating file: " + ex.toString());
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            Log.d("mylog", "Photofile not null");
            Uri photoURI = FileProvider.getUriForFile(UploadFaunaDetails.this, "com.vahapps.faunacare.fileprovider", photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, TAKE_PICTURE);
        }*/
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("reqcodeee",""+requestCode);
        Log.d("in result","yessss"+resultCode);

        if (data!=null)
        {
            Log.d("data","not null");
        }
        else {
            Log.d("data","null");
        }
        if (requestCode == TAKE_PICTURE && resultCode==RESULT_OK) {

            Bitmap photo = BitmapFactory.decodeFile(Uri.fromFile(outFile).getPath());
            Log.d("Width: ",""+photo.getWidth());
            Log.d("Height: ",""+photo.getHeight());
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(Uri.fromFile(outFile), "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 4);
            cropIntent.putExtra("aspectY", 3);
            //indicate output X and Y
            cropIntent.putExtra("outputX", photo.getWidth());
            cropIntent.putExtra("outputY", photo.getWidth()*3/4);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            outFile = new File(Environment.getExternalStorageDirectory() +"/faunapics/", newPicFile);
            Uri outuri = Uri.fromFile(outFile);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);

            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 33);
        }
        if(requestCode==33 && resultCode==RESULT_OK) {
            Log.d("in result ok", "yessss");
            Uri uri = null;
            if (data != null) {
                Log.d("uri from data", "yess");
                uri = data.getData();
            }
            if (uri == null && mCameraFileName != null) {
                uri = Uri.fromFile(new File(mCameraFileName));
            }
            Log.d("fileeee", uri.getPath());
            File file = new File(mCameraFileName);
            //if (!file.exists()) {
            file.mkdirs();
            //}
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(outFile));
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
