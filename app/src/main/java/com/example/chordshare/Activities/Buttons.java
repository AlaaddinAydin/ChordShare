package com.example.chordshare.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
                Intent goToEducation = new Intent(Buttons.this, Education.class);
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
}