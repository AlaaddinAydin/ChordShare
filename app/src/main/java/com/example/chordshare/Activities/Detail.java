package com.example.chordshare.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chordshare.R;

public class Detail extends AppCompatActivity {

    private ImageView musicImage , musicChord ;

    private TextView txtMusicName , txtGroupName, txtMusicLyrics;
    private String musicName,groupName,musicLyrics;
    private Bitmap tMusicImage,tMusicChord;

    private void init()
    {
        musicImage = (ImageView) findViewById(R.id.detail_imageView_musicImage);
        musicChord = (ImageView) findViewById(R.id.detail_imageView_musicChord);

        txtMusicName = (TextView) findViewById(R.id.detail_textView_musicName);
        txtGroupName = (TextView) findViewById(R.id.detail_textView_musicGroup);
        txtMusicLyrics = (TextView) findViewById(R.id.detail_textView_musicLyrics);

        musicName = Home.musicDetail.getMusicName();
        groupName = Home.musicDetail.getMusciGroup();
        musicLyrics = Home.musicDetail.getMusicLyrics();


        tMusicImage = Home.musicDetail.getMusicImage();
        tMusicChord = Home.musicDetail.getMusicChord();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);

        init();


        if(!TextUtils.isEmpty(musicName) && !TextUtils.isEmpty(groupName) && !TextUtils.isEmpty(musicLyrics))
        {
            txtMusicName.setText(musicName);
            txtGroupName.setText(groupName);
            txtMusicLyrics.setText(musicLyrics);

            musicImage.setImageBitmap(tMusicImage);
            musicChord.setImageBitmap(tMusicChord);
        }

    }
}