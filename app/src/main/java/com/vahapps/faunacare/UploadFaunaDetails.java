package com.vahapps.faunacare;

/**
 * Created by Vaibhavi on 15-Feb-18.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.os.Environment.getExternalStoragePublicDirectory;


public class UploadFaunaDetails extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = "HomeVet";
    //constant to track image chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    //view objects
    private ImageButton buttonChoose,btnLocate;
    private Button buttonUpload;
    private EditText editTextName,txtlocation;
    private TextView textViewShow;
    private ImageView imageView;
    private Spinner spinner;
    //uri to store file
    private Uri filePath;

    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 11;

    //firebase objects
    private FirebaseAuth mAuth;
    private FirebaseDatabase fdb;
    private StorageReference storageReference;
    private DatabaseReference mDatabase;
    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "Fauna";
    private String tempLat,tempLong;
    private String faunakey;
    private String userId;
    private String imageEncoded;
    private String mCameraFileName;
    private Uri mCapturedImageURI;
    private File outFile;
    //public String name;
    //public String url;
    AppLocationService appLocationService;
    final int TAKE_PICTURE = 1;
    final int ACTIVITY_SELECT_IMAGE = 2;
    private String newPicFile;
    private ViewManager viewManager;
    private Matrix matrix;
    private int size;
    private final int outputSize = 100;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload);

        buttonChoose = (ImageButton) findViewById(R.id.capture);
        buttonUpload = (Button) findViewById(R.id.upload);
        imageView = (ImageView) findViewById(R.id.ShowImageView);
        editTextName = (EditText) findViewById(R.id.desc);
        txtlocation= (EditText) findViewById(R.id.txtlocation);
        spinner = (Spinner) findViewById(R.id.spinner);
        btnLocate= (ImageButton) findViewById(R.id.faunaLocate);

        /*Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        size = width < height ? width : height;
        size -= 50;
        imageView.getLayoutParams().width = size;
        imageView.getLayoutParams().height = size;
        viewManager = (ViewManager) imageView.getParent();
*//*
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)) {
            createZoomControls();
        }*//*
        imageView.setOnTouchListener(new View.OnTouchListener() {
            float initX;
            float initY;
            float midX;
            float midY;
            float scale;
            float initDistance;
            float currentDistance;
            boolean isMultitouch = false;

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        initX = event.getX();
                        initY = event.getY();
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        isMultitouch = true;
                        initDistance = (float) Math.sqrt(Math.pow(
                                initX - event.getX(1), 2)
                                + Math.pow(initY - event.getY(1), 2));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isMultitouch) {
                            matrix = imageView.getImageMatrix();
                            currentDistance = (float) Math.sqrt(Math.pow(initX
                                    - event.getX(1), 2)
                                    + Math.pow(initY - event.getY(1), 2));
                            scale = 1 + 0.001f * (currentDistance - initDistance);
                            midX = 0.5f * (initX + event.getX(1));
                            midY = 0.5f * (initY + event.getY(1));
                            matrix.postScale(scale, scale, midX, midY);
                            imageView.setImageMatrix(matrix);
                            imageView.invalidate();
                        } else {
                            imageView.scrollBy((int) (initX - event.getX()),
                                    (int) (initY - event.getY()));
                            initX = event.getX();
                            initY = event.getY();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isMultitouch = false;
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        isMultitouch = false;
                        break;
                }
                return true;
            }
        });*/
        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("Fauna");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        //textViewShow.setOnClickListener(this);
        appLocationService = new AppLocationService(UploadFaunaDetails.this);
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        float[] distanceResults = new float[1];
        //Double t= Double.valueOf(tempLat);
        //Location.distanceBetween(Double.valueOf(tempLat),Double.valueOf(tempLong),Double.valueOf(tempLat),Double.valueOf(tempLat),distanceResults);
        Location.distanceBetween(23.256817500000004,72.64820703125004,23.248612500000014,72.64304296875,distanceResults);
        // String d=Float.toString(result[0]);
        double distanceKm = distanceResults[0]/1000; //* UnitConversions.M_TO_KM;
        //txtlocation.setText( String.valueOf(distanceKm));
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(UploadFaunaDetails.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }
   /* public void createZoomControls() {
        ZoomButtonsController zoomButtonsController = new ZoomButtonsController(imageView);
        zoomButtonsController.setVisible(true);
        zoomButtonsController.setAutoDismissed(false);
        zoomButtonsController.setOnZoomListener(new ZoomButtonsController.OnZoomListener() {

            public void onZoom(boolean zoomIn) {
                matrix = imageView.getImageMatrix();
                if (zoomIn) {
                    matrix.postScale(1.05f, 1.05f, 0.5f * size, 0.5f * size);
                    imageView.setImageMatrix(matrix);
                } else {
                    matrix.postScale(0.95f, 0.95f, 0.5f * size, 0.5f * size);
                    imageView.setImageMatrix(matrix);
                }
                imageView.invalidate();
            }

            public void onVisibilityChanged(boolean visible) {
            }
        });
        RelativeLayout.LayoutParams zoomLayoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        zoomLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        zoomLayoutParams.addRule(RelativeLayout.BELOW, R.id.ShowImageView);
        viewManager.addView(zoomButtonsController.getContainer(), zoomLayoutParams);
        zoomButtonsController.isAutoDismissed();
    }*/
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
    /*private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory *//*
        );

        // Save a file: path for use with ACTION_VIEW intents
        String mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("mylog", "Path: " + mCurrentPhotoPath);
        return image;
    }*/
    @Override
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

            /*Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            //imageView.setMinimumHeight(600);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);*/
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
            /*if (outFile.exists()){
                outFile.delete();
            }*/
            outFile = new File(Environment.getExternalStorageDirectory() +"/faunapics/", newPicFile);
            Uri outuri = Uri.fromFile(outFile);
            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);

            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 33);
        }
        if(requestCode==33 && resultCode==RESULT_OK){
            Log.d("in result ok","yessss");
            Uri uri = null;
            if (data != null) {
                Log.d("uri from data","yess");
                uri = data.getData();
            }
            if (uri == null && mCameraFileName != null) {
                uri = Uri.fromFile(new File(mCameraFileName));
            }
            Log.d("fileeee",uri.getPath());
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
            /*File imgFile = outFile;

            if(imgFile.exists()){
                Log.d("exitss","yess");
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);

            }*/

           /* String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(mCapturedImageURI, projection, null,
                            null, null);
            int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String picturePath = cursor.getString(column_index_data);
            Log.d("picturepathhh",picturePath);*/
        } /*if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
                tempLat = String.valueOf(place.getLatLng().latitude);
                tempLong = String.valueOf(place.getLatLng().longitude);
                String address = String.format("%s", place.getAddress());
                stBuilder.append(placename);
                stBuilder.append(address);
                txtlocation.setText(stBuilder.toString());
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        /*final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.show();
        //creating the objFauna object to store uploaded image details
        Fauna objFauna = new Fauna(userId,editTextName.getText().toString().trim(),spinner.getSelectedItem().toString(),imageEncoded,tempLat,tempLong,"1",txtlocation.getText().toString(),null,null,"0","0");
        //adding an objFauna to firebase database
        String uploadId = mDatabase.push().getKey();
        mDatabase.child(uploadId).setValue(objFauna);
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
        Intent i=new Intent(UploadFaunaDetails.this,Home.class);
        //i.putExtra("faunaKey",uploadId);
        startActivity(i);*/

        //checking if file is available
        if (Uri.fromFile(outFile) != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final String currentDate=DateFormat.getDateInstance().format(new Date());
            DateFormat df2 = new SimpleDateFormat("h:mm");
            final String currentTime=df2.format(new Date());
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
                            //dismissing the progress dialog
                            progressDialog.dismiss();
                            //displaying success toast
                            //creating the objFauna object to store uploaded image details
                            Fauna objFauna = new Fauna(userId,editTextName.getText().toString().trim(),spinner.getSelectedItem().toString(),taskSnapshot.getDownloadUrl().toString(),tempLat,tempLong,"1",txtlocation.getText().toString(),"","","0","0",currentDate,currentTime,"");
                            //adding an objFauna to firebase database
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(objFauna);

                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            Intent i=new Intent(UploadFaunaDetails.this,ChooseVol.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("faunaKey",uploadId);
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

    @Override
    public void onClick(View view) {
        if (view == buttonChoose) {
            showFileChooser();
            //gps lat n long
            mAuth = FirebaseAuth.getInstance();
            Location gpsLocation = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
            if (gpsLocation != null) {
                tempLat = String.valueOf(gpsLocation.getLatitude());
                tempLong = String.valueOf(gpsLocation.getLongitude());
            } else {
                //showSettingsAlert();
            }
            Location location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LocationAddress locationAddress = new LocationAddress();
                locationAddress.getAddressFromLocation(latitude, longitude, getApplicationContext(), new GeocoderHandler());
            } else {
                //showSettingsAlert();
            }
        } else if (view == buttonUpload) {
            uploadFile();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            txtlocation.setText(locationAddress);
        }
    }

    public static class LocationAddress {
        private static final String TAG = "com.gpslocation.MainActivity.LocationAddress";
        public static void getAddressFromLocation(final double latitude, final double longitude, final Context context, final Handler handler) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    String result = null;
                    try {
                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = addressList.get(0);
                            StringBuilder sb = new StringBuilder();
                            /*for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                sb.append(address.getAddressLine(i)).append("\n");
                            }*/
                            //sb.append(address.getFeatureName()).append("\n");
                            //sb.append(address.getSubLocality()).append("\n");
                            //sb.append(address.getLocality()).append("\n");
                            //sb.append(address.getSubAdminArea()).append("\n");
                            //sb.append(address.getAdminArea()).append("\n");
                            //sb.append(address.getCountryName());
                            //sb.append(address.getThoroughfare()).append("\n");
                            //sb.append(address.getLocale()).append("\n");
                            //sb.append(address.getPremises()).append("\n");
                            sb.append(address.getAddressLine(0)).append("\n");
                            //sb.append(address.getPostalCode()).append("\n");
                            //sb.append(address.getSubThoroughfare()).append("\n");
                            result = sb.toString();
                            Log.wtf("Locality",address.getLocality());
                            Log.wtf("AdminArea",address.getAdminArea());
                            Log.wtf("Feature",address.getFeatureName());
                            //Log.wtf("Locale",address.getLocale());
                            Log.wtf("Premises",address.getPremises());
                            Log.wtf("SubArea",address.getSubAdminArea());
                            Log.wtf("SubLocality",address.getSubLocality());
                            Log.wtf("SubThoroughfare",address.getSubThoroughfare());
                            Log.wtf("Thoroughfare",address.getThoroughfare());
                            Log.wtf("Addresline",address.getAddressLine(0));
                        }
                    } catch (IOException e) {
                        //  Log.e(TAG, "Unable connect to Geocoder", e);
                    } finally {
                        Message message = Message.obtain();
                        message.setTarget(handler);
                        if (result != null) {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            //result = "Latitude: " + latitude + " Longitude: " + longitude + "\n\nAddress:\n" + result;
                            bundle.putString("address", result);
                            message.setData(bundle);
                        } else {
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            //result = "Latitude: " + latitude + " Longitude: " + longitude + "\n Unable to get address for this lat-long.";
                            result = "Unable to get address for this lat-long.";
                            bundle.putString("address", result);
                            message.setData(bundle);
                        }
                        message.sendToTarget();
                    }
                }
            };
            thread.start();
        }
    }
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UploadFaunaDetails.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        UploadFaunaDetails.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop() {

        mGoogleApiClient.disconnect();
        super.onStop();
        //mAuth.signOut();
    }
}

