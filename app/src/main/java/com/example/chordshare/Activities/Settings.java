package com.example.chordshare.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chordshare.R;

import java.util.Objects;

public class Settings extends AppCompatActivity {

    SharedPreferences sharedPref;
    TextView txtFrequency;
    TextView txtDuration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Objects.requireNonNull(getSupportActionBar()).hide();

        sharedPref = this.getSharedPreferences(getString(R.string.shared_pref_name), Context.MODE_PRIVATE);

        // Back button
        ImageButton backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> finish());

        // Frequency
        txtFrequency = findViewById(R.id.txtFrequencySet);
        changeFrequency(0);
        Button btnMinusFrequency = findViewById(R.id.btnMinusFrequency);
        // on click, decrease by one
        btnMinusFrequency.setOnClickListener(v -> {
            changeFrequency(-1);
        });
        // on hold, lower by one every 100ms
        btnMinusFrequency.setOnLongClickListener(v -> {
            new Thread(() -> {
                while (btnMinusFrequency.isPressed()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(() -> changeFrequency(-1));
                }
            }).start();
            return true;
        });
        Button btnPlusFrequency = findViewById(R.id.btnPlusFrequency);
        // on click, increase by one
        btnPlusFrequency.setOnClickListener(v -> {
            changeFrequency(+1);
        });
        // on hold, increase by one every 100ms
        btnPlusFrequency.setOnLongClickListener(v -> {
            new Thread(() -> {
                while (btnPlusFrequency.isPressed()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(() -> changeFrequency(+1));
                }
            }).start();
            return true;
        });

        // Duration
        txtDuration = findViewById(R.id.txtDuration);
        changeDuration(0);
        Button btnMinusOnTime = (Button) findViewById(R.id.btnMinusDuration);
        // on click, decrease by one
        btnMinusOnTime.setOnClickListener(v -> {
            changeDuration(-1);
        });
        // on hold, lower by one every 100ms
        btnMinusOnTime.setOnLongClickListener(v -> {
            new Thread(() -> {
                while (btnMinusOnTime.isPressed()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(() -> changeDuration(-1));
                }
            }).start();
            return true;
        });
        Button btnPlusDuration = findViewById(R.id.btnPlusDuration);
        // on click, increase by one
        btnPlusDuration.setOnClickListener(v -> {
            changeDuration(1);
        });
        // on hold, increase by one every 100ms
        btnPlusDuration.setOnLongClickListener(v -> {
            new Thread(() -> {
                while (btnPlusDuration.isPressed()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(() -> changeDuration(1));
                }
            }).start();
            return true;
        });
    }

    /**
     * Change the frequency by the given amount.
     * E.g. can be 1 to increase it by 1 or -1 to decrease it by -1
     * @param amount how much do increase/decrease
     */
    private void changeFrequency(int amount){
        int bpm = sharedPref.getInt(getString(R.string.shared_pref_frequency), 100);
        if ((bpm <= 0 && amount < 0) || (bpm >= 1000 && amount >= 0)){
            return;
        }
        bpm += amount;
        sharedPref.edit().putInt(getString(R.string.shared_pref_frequency), bpm).apply();
        txtFrequency.setText(getString(R.string.bpm, bpm));
    }

    /**
     * Change the duration by the given amount.
     * E.g. can be 1 to increase it by 1 or -1 to decrease it by -1
     * @param amount how much do increase/decrease
     */
    private void changeDuration(int amount){
        int duration = sharedPref.getInt(getString(R.string.shared_pref_duration), 250);
        if ((duration <= 0 && amount < 0) || (duration >= 1000 && amount > 0)){
            return;
        }
        duration += amount;
        sharedPref.edit().putInt(getString(R.string.shared_pref_duration), duration).apply();
        txtDuration.setText(getString(R.string.duration, duration));
    }
}