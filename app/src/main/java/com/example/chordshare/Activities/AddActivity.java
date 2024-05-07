package com.example.chordshare.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chordshare.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddActivity extends AppCompatActivity {


    private EditText editTextMusicName , editTextMusicGrup, editTextMusicLyrics;
    private ImageView imageViewMusicChord , imageViewMusicImage;
    private String musicName, musicGroup, musiclyrics;
    //private Button btnSave;

    private boolean isMainImg;

    private static final int REQUEST_PERMISSION_CODE = 1001;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    private Bitmap selectedImage, resizeMusicImage , resizeMusicChord;


    private void init(){

        editTextMusicGrup = (EditText) findViewById(R.id.add_activity_editTextMusicGroup);
        editTextMusicName = (EditText) findViewById(R.id.add_activity_editTextMusicName);
        editTextMusicLyrics = (EditText) findViewById(R.id.add_activity_editTextMusicLyrics);

        imageViewMusicChord = (ImageView) findViewById(R.id.add_activity_imageViewMusicChord);
        imageViewMusicImage = (ImageView) findViewById(R.id.add_activity_imageViewMusicImage);

        //btnSave = (Button) findViewById(R.id.add_activity_btnSave);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //İnitialize edilmesi için metot kullandım
        init();


        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                // Permission granted, launch image picker
                pickImage();
            } else {
                // Permission denied, inform the user
                showToast("External storage permission is required to select an image.");
            }
        });

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                // Image picked, get Uri and display the image
                Uri imageUri = result.getData().getData();
                try {
                    if (Build.VERSION.SDK_INT >= 28) {
                        ImageDecoder.Source imageSource = ImageDecoder.createSource(getContentResolver(), imageUri);
                        selectedImage = ImageDecoder.decodeBitmap(imageSource);
                    } else {
                        selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    }

                    if(isMainImg)
                    {
                        imageViewMusicImage.setImageBitmap(selectedImage);
                    }else
                    {
                        imageViewMusicChord.setImageBitmap(selectedImage);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    showToast("Failed to load selected image.");
                }
            }
        });
    }



    public void MusicSave(View v){

        musicName = editTextMusicName.getText().toString();
        musicGroup = editTextMusicGrup.getText().toString();
        musiclyrics = editTextMusicLyrics.getText().toString();

        if(!TextUtils.isEmpty(musicName) && !TextUtils.isEmpty(musicGroup) && !TextUtils.isEmpty(musiclyrics))
        {
            // Kaydediliyor

            Drawable musicImageDrawable = imageViewMusicImage.getDrawable();
            Drawable musicChordDrawable = imageViewMusicChord.getDrawable();

            if (musicImageDrawable instanceof BitmapDrawable && musicChordDrawable instanceof BitmapDrawable) {
                ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
                ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();


                Bitmap musicImage = ((BitmapDrawable) imageViewMusicImage.getDrawable()).getBitmap();
                Bitmap musicChord = ((BitmapDrawable) imageViewMusicChord.getDrawable()).getBitmap();

                resizeMusicImage = resizeImage(musicImage);
                resizeMusicChord = resizeImage(musicChord);

                resizeMusicImage.compress(Bitmap.CompressFormat.PNG, 100, outputStream1);
                byte[] musicImageToSave = outputStream1.toByteArray();

                resizeMusicChord.compress(Bitmap.CompressFormat.JPEG, 100, outputStream2);
                byte[] musicChordToSave = outputStream2.toByteArray();

                try {
                    SQLiteDatabase database = this.openOrCreateDatabase("Musics",MODE_PRIVATE,null);
                    database.execSQL("CREATE TABLE IF NOT EXISTS musics (id INTEGER PRIMARY KEY , musicName VARCHAR, musicGroup VARCHAR, musicLyrics VARCHAR , musicImage BLOB, musicChord BLOB)");

                    String sqlQuery = "INSERT INTO musics (musicName , musicGroup, musicLyrics , musicImage , musicChord) VALUES (?, ?, ?, ?, ?)";
                    SQLiteStatement statement = database.compileStatement(sqlQuery);

                    statement.bindString(1 , musicName);
                    statement.bindString(2 , musicGroup);
                    statement.bindString(3 , musiclyrics);
                    statement.bindBlob(4 , musicImageToSave);
                    statement.bindBlob(5 , musicChordToSave);

                    statement.execute();


                    clearObjects();


                    showToast("Ekleme işlemi başarılı !");



                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                // Resimleri kullanarak işlemleri devam ettirin
            } else {
                showToast("Lütfen resimleri yüklediğinizden emin olun");
            }


        }
        else {
            showToast("Boşlukları doldurduğunuzdan ve resim yüklediğinizden emin olun");
        }
    }

    private Bitmap resizeImage(Bitmap image){
        int maxWidth = 240;
        int maxHeight = 300;

        int width = image.getWidth();
        int height = image.getHeight();

        float scaleWidth = ((float) maxWidth) / width;
        float scaleHeight = ((float) maxHeight) / height;
        float scale = Math.min(scaleWidth, scaleHeight);

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(image, 0, 0, width, height, matrix, false);
    }

    private void showToast(String message)
    {
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }

    private void clearObjects()
    {
        editTextMusicGrup.setText("");
        editTextMusicName.setText("");
        editTextMusicLyrics.setText("");
        imageViewMusicChord.setImageResource(R.drawable.ic_image_search);
        imageViewMusicImage.setImageResource(R.drawable.ic_image_search);
    }


    public void selectMainImage(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            // Permission is granted, launch image picker
            isMainImg = true;
            pickImage();
        }
    }

    public void selectChordImage(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            // Permission is granted, launch image picker
            isMainImg = false;
            pickImage();
        }
    }

    private void pickImage() {
        Intent takeImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(takeImage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent = new Intent(this, Home.class);
        finish();
        startActivity(backIntent);
    }
}