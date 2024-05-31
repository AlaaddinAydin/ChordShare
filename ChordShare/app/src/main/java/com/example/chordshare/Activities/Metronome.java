package com.example.chordshare.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.chordshare.R;

import java.util.Objects;

public class Metronome extends AppCompatActivity {

    TextView TxtFrequency;
    Boolean isRunning = false;
    Vibrator vibrator;
    Button btnStartStop;
    int bpm = 100;
    int duration = 250;
    PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metronome);

        Objects.requireNonNull(getSupportActionBar()).hide();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // Frequency
        TxtFrequency = (TextView) findViewById(R.id.txtFrequency);

        // Start/Stop button
        btnStartStop = (Button) findViewById(R.id.btnStartStop);
        btnStartStop.setOnClickListener(v -> {
            isRunning = !isRunning;
            if (isRunning) {
                startVibration();
            } else {
                stopVibration();
            }
        });

        // Settings button
        ImageButton settingsButton = (ImageButton) findViewById(R.id.btnSettings);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(Metronome.this, Settings.class);
            startActivity(intent);
        });

        //get the wake lock
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,"SmartwatchMetronome:keepAwake");
    }

    /**
     * When screen is displayed again. Used to update frequency and duration
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.shared_pref_name), MODE_PRIVATE);
        bpm = sharedPref.getInt(getString(R.string.shared_pref_frequency), 100);
        duration = sharedPref.getInt(getString(R.string.shared_pref_duration), 250);
        TxtFrequency.setText(String.valueOf(bpm) + " BPM");
    }

    /**
     * When screen is not displayed anymore. Used to stop vibration
     */
    @Override
    protected void onStop() {
        super.onStop();
        stopVibration();
    }

    /**
     * Start to vibrate and set text and color of Start/Stop-Button
     */
    private void startVibration(){
        btnStartStop.setText("Stop");
        btnStartStop.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
        //calculate pattern by using bpm
        int beatDuration = Math.round(60000f / bpm); // duration of each beat in milliseconds
        int vibrationDuration = duration; // duration of vibration in milliseconds
        int intervalDuration = beatDuration - vibrationDuration; // duration of interval between vibrations
        long[] pattern = {0, vibrationDuration, intervalDuration}; // pattern with 1 beat
        wakeLock.acquire(10*60*1000L /*10 minutes*/);
        vibrator.vibrate(pattern, 0);
    }

    /**
     * Stops to vibrate and set text and color of Start/Stop-Button
     */
    private void stopVibration(){
        btnStartStop.setText("Start");
        btnStartStop.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_green_light)));
        vibrator.cancel();
        if (wakeLock.isHeld())
            wakeLock.release();
    }

}