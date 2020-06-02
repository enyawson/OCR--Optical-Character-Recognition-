package com.example.ocr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ocr.Entities.VoterID;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    /*Card variables to hold splitted data */
    /*Name of individual*/
    //stores name written in full on card eg. NHIS
    String fullName;
    //Stores surname
    String surname;
    //Stores firName
    String firstName;
    //stores middleName
    String middleName;
    //stores given name for passport
    String givenName;

    //stores place of issue for passport
    String placeOfIssue;
    //stores the authorize body
    String authorizeBody;
    //stores country code
    String countryCode;
    String country;

    //stores the sex of the user
    char sex;
    //stores passport type
    char passportType;


    /*Identification Numbers*/
    //stores National ID
    String nationalId;
    //stores membership Number for NHIS
    String membershipNumber;
    //stores passport Number
    String passportNumber;
    String ssnitNumber;

    /*Date*/
    //stores date of birth
    String dateOfBirth;
    //stores Date of expiry for National ID, NHIS
    String expiryDate;
    //stores the date of issue
    String issueDate;

    
    //add image , button, text view variables
    ImageView previewImageView;
    ImageView addPhotoImageView;
    ImageView takePhotoImageView;
    TextView resultTextView;
    LinearLayout imageSelectionOption;
    TextView showTypeOfCard;
    Button saveButton;

    //add request code
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;

    //camera and storage permission variable
    String cameraPermission[];
    String storagePermission[];

    //image view identifier
    Uri image_uri;
    //crop image
    Uri resultUri;

    //builds a comma delimited string
    StringBuilder stringBuilder = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //camera permission
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //storage permission
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //pass image view id in xml to preview variable
        previewImageView = findViewById(R.id.image_view);
        //pass text view id in xml to result view variable
        resultTextView = findViewById(R.id.result_text_view);
        //pass id of linear layout for image select option
        imageSelectionOption = findViewById(R.id.image_options_linear_layout);

        //pass add_photo image view id to variable
        addPhotoImageView = findViewById(R.id.add_image_view);
        //set onclick listener on add photo to pick an image from gallery
        addPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkStoragePermission()) {
                    //storage permission not allowed , request it
                    requestStoragePermission();
                } else {
                    //permission allowed get image from device
                    getImageOnDevice();
                }

            }
        });

        //pass take_photo image view id to variable
        takePhotoImageView = findViewById(R.id.take_photo_image_view);
        takePhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkCameraPermission()) {
                    //camera permission not allowed , request it
                    requestCameraPermission();
                } else {
                    //permission allowed capture image with device camera
                    getDeviceCamera();
                }
            }
        });

        //set button id
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save data retrieved in database
                addRecords();
                Log.i(TAG, "Data saved");
                Toast.makeText(MainActivity.this, "Data Saved", Toast.LENGTH_LONG)
                        .show();
            }
        });
        //pass id of show type of card to view
        showTypeOfCard = findViewById(R.id.typeOfCard_text_view);
    }


    /**
     * This method grants the permission to access device camera
     */
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    /**
     * This method checks if permission to access camera is granted
     */
    private boolean checkCameraPermission() {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission
                .CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return result1 && result2;
    }


    /**
     * This method grant permission to access image on the device
     */
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }


    /**
     * This method checks if storage permission is granted
     */
    private boolean checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ;
        return true;
    }


    /**
     * This method create an intent that starts an activity to get
     * device camera.
     */
    private void getDeviceCamera() {
        //create device storage for pic taken, add details to pic
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");//title of image
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text");//description
        //uniform resource identifier gives image taken an id and values above
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);

        //intent to open camera activity to capture image
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
            startActivityForResult(takePictureIntent, IMAGE_PICK_CAMERA_CODE);
        }
    }


    /**
     * This method create an intent that starts an activity to get
     * images on device.
     */
    private void getImageOnDevice() {
        //intent to pick image from gallery or device images
        Intent picIntent = new Intent(Intent.ACTION_PICK);
        //set intent type to image
        picIntent.setType("image/*");
        startActivityForResult(picIntent, IMAGE_PICK_GALLERY_CODE);

    }


    /**
     * This method handles the result of the permissions for the first time
     *
     * @param requestCode  the requested code for permissions to be granted
     * @param permissions  are the permissions asked to access camera and gallery
     * @param grantResults is the result of permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        getDeviceCamera();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }

            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        getImageOnDevice();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }


    /**
     * This method handles the image gotten from the captured camera, or gallery.
     * it helps to crop image and uses the MLKit vision api to recognize text on image.
     *
     * @param requestCode the requested code for permissions to be granted
     * @param resultCode  The result after permission is granted and image gotten or captured with
     *                    camera
     * @param data        The data retrieved from intent, either from the camera activity or the
     *                    gallery activity
     */
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //crop image gotten from gallery
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);//enable image guidelines

            }
            //crop image taken with camera
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);//enable image guidelines
            }
            //get the crop image
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    //get crop image uri
                    Uri resultUri = result.getUri();
                    //set crop image to preview image view

                    previewImageView.setImageURI(resultUri);
                    //remove container of image upload options
                    //imageSelectionOption.setVisibility(View.GONE);


                    //run image recognition on crop image
                    runTextRecognition();


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //show error
                    Exception error = result.getError();
                    Toast.makeText(this, " " + error, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    /**
     * This method recognize text in blocks
     */
    private void runTextRecognition() {
        //get crop image
        BitmapDrawable bitmapDrawable = (BitmapDrawable) previewImageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        //firebase recognition
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        //pass the image to the processImage method
        textRecognizer.processImage(firebaseVisionImage)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText result) {
                        //extract text from image
                        processTextRecognitionResult(result);
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //print error
                                Toast.makeText(MainActivity
                                                .this, "Retrieving Text from not successful",
                                        Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                );
    }

    /**
     * This method extracts the text from firebase vision text blocks of
     * recognize text
     *
     * @param result recognize text
     */
    private void processTextRecognitionResult(FirebaseVisionText result) {
        for (FirebaseVisionText.TextBlock block : result.getTextBlocks()) {
            for (FirebaseVisionText.Line line : block.getLines()) {
                    stringBuilder.append(line.getText());
//                    String elementText = element.getText();
                    //String cardName = elementText.toUpperCase();
//                    Log.i(TAG, "ELEMENT_TEXT" + elementText);
                    stringBuilder.append(",");

            }
        }
        //set result text view to string builder text
        resultTextView.setText(stringBuilder.toString());
        saveButton.setVisibility(View.VISIBLE);
        imageSelectionOption.setVisibility(View.GONE);


        //This method splits the string builder text generated by the fire base vision text
        //and displays the type of card being used
        DataRetrieval dataRetrieval = new DataRetrieval();
        dataRetrieval.setStringToSplit(stringBuilder.toString());
        showTypeOfCard.setText(dataRetrieval.identifyCardType());
        Log.i(TAG, "DISPLAY" + showTypeOfCard);

//        identifyCardType(stringBuilder);
//        Log.i(TAG, "CARD" + stringBuilder);
//        List<String> list = new ArrayList(Arrays.asList(stringBuilder.toString()));
//        Log.v(TAG, "RETRIEVED TEXT" + voterCardName);

    }



   /*private void identifyCardType(StringBuilder text) {
        String[] splitString = text.toString().split(",");
        for (String string : splitString){
            //convert string to upper case
            String splittedString = string.toLowerCase();
            //check for the type of card being used.
            if(splittedString.contains("voter") ){
                typeOfCard = "Voter ID Card";
                showTypeOfCard.setText(typeOfCard);
            }else if(splittedString.contains("ssnit") ){
                typeOfCard = " SSNIT CARD";
                showTypeOfCard.setText(typeOfCard);
            }else if(splittedString.contains("identity") ){
                typeOfCard = " National Identity Card";
                showTypeOfCard.setText(typeOfCard);
            }else if(splittedString.contains("ecowas") ){
                typeOfCard = " ECOWAS CARD";
                showTypeOfCard.setText(typeOfCard);
            }else if(splittedString.contains("nhis") ){
                typeOfCard = "NHIS Card";
                showTypeOfCard.setText(typeOfCard);
            }else if(splittedString.contains("driver") ){
                typeOfCard = "Driver Licence";
                showTypeOfCard.setText(typeOfCard);
            }else if(splittedString.contains("passport") ){
                typeOfCard = "PASSPORT";
                showTypeOfCard.setText(typeOfCard);
            }
            break;
        }
       Log.i(TAG, "VOTERS" + typeOfCard);
    }*/


    /**
     * This method sets retrieve data to database object variables
     *
     */
    private void insertRecords(){
        //instantiate database objects

         VoterID voterID = new VoterID();
        //NHIS nhis = new NHIS();
        //Passport passport = new Passport();
        //DriverLicence driverLicence = new DriverLicence();
        //SSNIT ssnit = new SSNIT();
        //EcowasID ecowasID = new EcowasID();
        //NationalID nationalID = new NationalID();

        /*retrieve data from stringbuilder into database object variables*/
//        switch(typeOfCard){
//            case "voter id":
//        }

        /*save data*/
        //voterID.save();...

        /*set result*/
        //setResult(RESULT_OK);

        /*Toast.makeTest(MainActivity.this, "record Inserted",
        Toast.LENGTH_LONG).show();*/

        /*clear text views*/

    }

    /**
     * This method insert data retrieve into database
     *
     */
    private void addRecords(){
    // insert Records
        //insertRecords();
    }

}
