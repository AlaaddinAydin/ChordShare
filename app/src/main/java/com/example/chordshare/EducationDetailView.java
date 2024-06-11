package com.example.chordshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chordshare.Activities.Detail;
import com.example.chordshare.Activities.EducationMain;
import com.example.chordshare.Activities.Home;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class EducationDetailView extends AppCompatActivity {


    private TextView txtEducationName , txtEducationDescription;

    private String educationName,educationDescription;

    private String TAG = "MainActivity";
    private String NEW_VIDEO_ID = EducationMain.educationDetail.getEducationLink();

    private int position;

    private Button deleteButton;

    private void init()
    {
        txtEducationName = (TextView) findViewById(R.id.detail_textView_educationName);
        txtEducationDescription = (TextView) findViewById(R.id.detail_textView_educationDescription);

        deleteButton = (Button) findViewById(R.id.education_delete_button);



        educationName = EducationMain.educationDetail.getEducationName();
        educationDescription = EducationMain.educationDetail.getEducationDescription();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_detail_view);

        init();

        YouTubePlayerView youTubePlayerView = findViewById(R.id.education_youtube_player_view);

        if (!TextUtils.isEmpty(educationName) && !TextUtils.isEmpty(educationDescription)) {
            txtEducationName.setText(educationName);
            txtEducationDescription.setText(educationDescription);


            getLifecycle().addObserver(youTubePlayerView);

        }

        position = getIntent().getIntExtra("position", -1); // Pozisyonu al
        if (position == -1) {
            showToast("Hata: Pozisyon alınamadı.");
        }


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEducation();
            }
        });

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                youTubePlayer.loadVideo(NEW_VIDEO_ID, 0);
            }

            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                Log.d(TAG, "YouTube player hazır.");

                // Yeni videoyu yükle
                youTubePlayer.loadVideo(NEW_VIDEO_ID, 0);
            }
        });






    }



    private void deleteEducation() {
        // Silme işlemi burada gerçekleştirilecek
        // Örneğin, müzik öğesinin konumunu al ve adapter üzerinden silme işlemini gerçekleştir
        int position = getIntent().getIntExtra("position", -1);
        if (position != -1) {
            try {
                SQLiteDatabase database = this.openOrCreateDatabase("Educations", MODE_PRIVATE, null);
                database.execSQL("DELETE FROM educations WHERE educationName = '" + educationName + "' AND educationDescription = '" + educationDescription + "'");

                // Silme işlemi gerçekleştirildikten sonra bir mesaj gösterebilirsiniz
                showToast("Müzik başarıyla silindi.");

                // Geri dönmeden doğrudan Home aktivitesine geç
                Intent intent = new Intent(EducationDetailView.this, EducationMain.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                showToast("Müzik silinemedi.");
            }
            showToast("Müzik başarıyla silindi.");

            Intent intent = new Intent(EducationDetailView.this, EducationMain.class);
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