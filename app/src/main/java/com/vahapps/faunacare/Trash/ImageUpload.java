/*
package com.vahapps.faunacare.Trash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vahapps.faunacare.R;

public class ImageUpload extends AppCompatActivity {
    private static String  Tag = "main activty";
    private Button mUploadBtn  ;
    private ImageButton mCaptureBtn;
    public  static final int CAMERA_REQUEST_CODE =1;
    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private ImageView SelectImage;
    public Uri FilePathUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload);
        mStorage = FirebaseStorage.getInstance().getReference();
        //mUploadBtn=(Button)findViewById(R.id.upload);
        mCaptureBtn= (ImageButton) findViewById(R.id.capture);
        SelectImage = (ImageView)findViewById(R.id.ShowImageView);

        mProgress=new ProgressDialog(this);
        mCaptureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE  && resultCode == RESULT_OK) {
            mProgress.setMessage("Uploading img");
            mProgress.show();
            Uri uri=data.getData();
            StorageReference fp=mStorage.child("Photos").child(uri.getLastPathSegment());
            fp.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    Toast.makeText(ImageUpload.this, "done", Toast.LENGTH_LONG).show();
                }
            });
            Log.d(Tag, "onCreate() Restoring previous state");
            Log.d(Tag,"File path uri: "+FilePathUri);
        }
    }
}*/
