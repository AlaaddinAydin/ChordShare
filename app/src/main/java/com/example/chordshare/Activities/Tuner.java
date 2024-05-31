package com.example.chordshare.Activities;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.chordshare.PitchView;
import com.example.chordshare.R;

import org.jtransforms.fft.DoubleFFT_1D;

import java.util.Locale;

public class Tuner extends Activity {

    private static final String TAG = "GuitarTuner";

    // Ses kaydı parametreleri
    private static final int SAMPLE_RATE = 44100; // Örnek hızı (Hz)
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO; // Tek kanallı ses girişi
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT; // PCM 16-bit formatı
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final double FREQUENCY_THRESHOLD = 10.0; // Hz

    // Ses kaydı ve işleme için değişkenler
    private AudioRecord audioRecord;
    private boolean isRecording = false;

    private TextView pitchLabel;
    private PitchView pitchView;

    // Eşik değeri (dB cinsinden)
    private static final double THRESHOLD_DB = 40.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuner);

        // XML dosyasında belirtilen bileşenleri tanımla
        pitchLabel = findViewById(R.id.pitch_label);
        pitchView = findViewById(R.id.pitch_view);

        // Ses kaydı başlat
        startRecording();
    }

    private void startRecording() {
        String permission = Manifest.permission.RECORD_AUDIO;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // İzin yoksa, kullanıcıdan izin iste
            ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            // İzin varsa, AudioRecord nesnesini oluştur ve kaydı başlat
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
            audioRecord.startRecording();
            isRecording = true;

            // Ses işleme döngüsü
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRecording) {
                        // Ses verilerini oku
                        short[] audioData = new short[BUFFER_SIZE / 2];
                        int bytesRead = audioRecord.read(audioData, 0, audioData.length);
                        if (bytesRead > 0) {
                            // Ses verilerini işle
                            processAudioData(audioData);
                        }
                    }
                }
            }).start();
        }
    }

    private void processAudioData(short[] audioData) {
        double[] audioDataDouble = new double[audioData.length];
        for (int i = 0; i < audioData.length; i++) {
            audioDataDouble[i] = audioData[i];
        }

        DoubleFFT_1D fft = new DoubleFFT_1D(audioDataDouble.length);
        fft.realForward(audioDataDouble);

        processFrequencies(audioDataDouble);
    }

    private void processFrequencies(double[] audioDataDouble) {
        double maxPower = Double.NEGATIVE_INFINITY;
        double dominantFrequencyValue = -1;

        for (int i = 0; i < audioDataDouble.length / 2; i++) {
            double real = audioDataDouble[2 * i];
            double imaginary = audioDataDouble[2 * i + 1];
            double power = real * real + imaginary * imaginary;
            if (power > maxPower) {
                maxPower = power;
                dominantFrequencyValue = i * (double) SAMPLE_RATE / audioDataDouble.length;
            }
        }

        final double finalDominantFrequencyValue = dominantFrequencyValue;
        double powerDb = 10 * Math.log10(maxPower);

        if (powerDb > THRESHOLD_DB) { // Belirli bir eşik değerine göre işlem yap
            String note = getNoteName(finalDominantFrequencyValue);
            final String finalNote = note;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pitchLabel.setText(finalNote);
                    pitchView.setCenterPitch(getFrequencyForNoteName(finalNote));
                    pitchView.setCurrentPitch((float) finalDominantFrequencyValue);
                }
            });
        }
    }

    private String getNoteName(double frequency) {
        double[] noteFrequencies = {65.41, 69.30, 73.42, 77.78, 82.41, 87.31, 92.50, 98.00, 103.83, 110.00,
                116.54, 123.47, 130.81, 138.59, 146.83, 155.56, 164.81, 174.61, 185.00, 196.00, 207.65, 220.00, 233.08,
                246.94, 261.63, 277.18, 293.66, 311.13, 329.63, 349.23, 369.99, 392.00, 415.30, 440.00, 466.16, 493.88,
                523.25, 554.37, 587.33, 622.25, 659.25, 698.46, 739.99, 783.99, 830.61};

        String[] noteNames = {"C2", "C#2", "D2", "D#2", "E2", "F2", "F#2", "G2", "G#2", "A2", "A#2", "B2", "C3",
                "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3", "C4", "C#4", "D4", "D#4", "E4", "F4",
                "F#4", "G4", "G#4", "A4", "A#4", "B4", "C5", "C#5", "D5", "D#5", "E5", "F5", "F#5", "G5", "G#5", "A5", "A#5",
                "B5", "C6"};

        double minDiff = Double.MAX_VALUE;
        int minDiffIndex = 0;
        for (int i = 0; i < noteFrequencies.length; i++) {
            double diff = Math.abs(frequency - noteFrequencies[i]);
            if (diff < minDiff) {
                minDiff = diff;
                minDiffIndex = i;
            }
        }

        return noteNames[minDiffIndex];
    }

    private float getFrequencyForNoteName(String noteName) {
        double[] noteFrequencies = {65.41, 69.30, 73.42, 77.78, 82.41, 87.31, 92.50, 98.00, 103.83, 110.00,
                116.54, 123.47, 130.81, 138.59, 146.83, 155.56, 164.81, 174.61, 185.00, 196.00, 207.65, 220.00, 233.08,
                246.94, 261.63, 277.18, 293.66, 311.13, 329.63, 349.23, 369.99, 392.00, 415.30, 440.00, 466.16, 493.88,
                523.25, 554.37, 587.33, 622.25, 659.25, 698.46, 739.99, 783.99, 830.61};

        String[] noteNames = {"C2", "C#2", "D2", "D#2", "E2", "F2", "F#2", "G2", "G#2", "A2", "A#2", "B2", "C3",
                "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3", "C4", "C#4", "D4", "D#4", "E4", "F4",
                "F#4", "G4", "G#4", "A4", "A#4", "B4", "C5", "C#5", "D5", "D#5", "E5", "F5", "F#5", "G5", "G#5", "A5", "A#5",
                "B5", "C6"};

        for (int i = 0; i < noteNames.length; i++) {
            if (noteNames[i].equals(noteName)) {
                return (float) noteFrequencies[i];
            }
        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (audioRecord != null) {
            isRecording = false;
            audioRecord.stop();
            audioRecord.release();
        }
    }
}
