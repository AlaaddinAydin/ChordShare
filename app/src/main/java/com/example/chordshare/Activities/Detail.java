package com.example.chordshare.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chordshare.Adapters.MusicAdapter;
import com.example.chordshare.Music;
import com.example.chordshare.R;

public class Detail extends AppCompatActivity {

    private ImageView musicImage , musicChord ;

    private TextView txtMusicName , txtGroupName, txtMusicLyrics;
    private String musicName,groupName,musicLyrics;

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


        if(!TextUtils.isEmpty(musicName) && !TextUtils.isEmpty(groupName) && !TextUtils.isEmpty(musicLyrics))
        {
            txtMusicName.setText(musicName);
            txtGroupName.setText(groupName);
            txtMusicLyrics.setText(musicLyrics);

            musicImage.setImageBitmap(tMusicImage);
            musicChord.setImageBitmap(tMusicChord);
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