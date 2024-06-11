package com.example.chordshare.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.chordshare.R;

public class Buttons extends AppCompatActivity {

    LinearLayout layout_education, layout_chord_database, layout_metronome, layout_tuner ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);

        layout_education = (LinearLayout) findViewById(R.id.layout_education);
        layout_chord_database = (LinearLayout) findViewById(R.id.layout_chord_database);
        layout_metronome = (LinearLayout) findViewById(R.id.layout_metronome);
        layout_tuner = (LinearLayout) findViewById(R.id.layout_tuner);


        layout_education.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToEducation = new Intent(Buttons.this, EducationMain.class);
                startActivity(goToEducation);
            }
        });


        layout_chord_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToEducation = new Intent(Buttons.this,Home.class);
                startActivity(goToEducation);
            }
        });



        layout_metronome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToEducation = new Intent(Buttons.this, Metronome.class);
                startActivity(goToEducation);
            }
        });


        layout_tuner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToEducation = new Intent(Buttons.this, Tuner.class);
                startActivity(goToEducation);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.buttons_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.aboutUs) {
            // Intent geçiş
            Intent addMusicIntent = new Intent(this, About.class);
            finish();
            startActivity(addMusicIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}