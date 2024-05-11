package com.example.chordshare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chordshare.Adapters.MusicAdapter;
import com.example.chordshare.Music;
import com.example.chordshare.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class Detail extends AppCompatActivity {

    private ImageView musicImage , musicChord ;

    private TextView txtMusicName , txtGroupName, txtMusicLyrics;
    private String musicName,groupName,musicLyrics, musicLink;

    private static final String TAG = "MainActivity";
    private static final String NEW_VIDEO_ID = Home.musicDetail.getMusicLink();

    private int position;

    private Button deleteButton;
    private Bitmap tMusicImage,tMusicChord;

    private void init()
    {
        musicImage = (ImageView) findViewById(R.id.detail_imageView_musicImage);
        musicChord = (ImageView) findViewById(R.id.detail_imageView_musicChord);

        txtMusicName = (TextView) findViewById(R.id.detail_textView_musicName);
        txtGroupName = (TextView) findViewById(R.id.detail_textView_musicGroup);
        txtMusicLyrics = (TextView) findViewById(R.id.detail_textView_musicLyrics);

        deleteButton = (Button) findViewById(R.id.delete_button);


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


        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);

        if (!TextUtils.isEmpty(musicName) && !TextUtils.isEmpty(groupName) && !TextUtils.isEmpty(musicLyrics)) {
            txtMusicName.setText(musicName);
            txtGroupName.setText(groupName);
            txtMusicLyrics.setText(musicLyrics);

            musicImage.setImageBitmap(tMusicImage);
            musicChord.setImageBitmap(tMusicChord);


            getLifecycle().addObserver(youTubePlayerView);

        }

        position = getIntent().getIntExtra("position", -1); // Pozisyonu al
        if (position == -1) {
            showToast("Hata: Pozisyon alınamadı.");
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMusic();
            }
        });

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                super.onReady(youTubePlayer);
                Log.d(TAG, "YouTube player hazır.");

                // Yeni videoyu yükle
                youTubePlayer.loadVideo(NEW_VIDEO_ID, 0);


            }
        });

    }


    private void deleteMusic() {
        // Silme işlemi burada gerçekleştirilecek
        // Örneğin, müzik öğesinin konumunu al ve adapter üzerinden silme işlemini gerçekleştir
        int position = getIntent().getIntExtra("position", -1);
        if (position != -1) {
            try {
                SQLiteDatabase database = this.openOrCreateDatabase("Musics", MODE_PRIVATE, null);
                database.execSQL("DELETE FROM musics WHERE musicName = '" + musicName + "' AND musicGroup = '" + groupName + "' AND musicLyrics = '" + musicLyrics + "'");

                // Silme işlemi gerçekleştirildikten sonra bir mesaj gösterebilirsiniz
                showToast("Müzik başarıyla silindi.");

                // Geri dönmeden doğrudan Home aktivitesine geç
                Intent intent = new Intent(Detail.this, Home.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Müzik silinemedi.");
            }
            showToast("Müzik başarıyla silindi.");

            Intent intent = new Intent(Detail.this, Home.class);
            startActivity(intent);
            finish();
        } else {
            showToast("Müzik silinemedi.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}