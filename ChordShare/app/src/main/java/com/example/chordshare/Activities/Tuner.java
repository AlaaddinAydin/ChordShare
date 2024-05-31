package com.example.chordshare.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.chordshare.PitchView;
import com.example.chordshare.R;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.android.service.PdService;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.PdListener;
import org.puredata.core.utils.IoUtils;

public class Tuner extends Activity implements OnClickListener {

    private static final String TAG = "GuitarTuner";

    //
    private PdUiDispatcher dispatcher;

    // Buttons
    private Button eButton;
    private Button aButton;
    private Button dButton;
    private Button gButton;
    private Button bButton;
    private Button eeButton;

    //
    private TextView pitchLabel;

    //
    private PitchView pitchView;

    //
    private PdService pdService = null;


    private final ServiceConnection pdConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pdService = ((PdService.PdBinder)service).getService();
            try {
                initPd();
                loadPatch();
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // this method will never be called
        }

    };


    //
    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(pdConnection);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e( TAG, "onCreate method.");
        super.onCreate(savedInstanceState);
        initGui();
        initSystemServices();
        bindService(new Intent(this, PdService.class), pdConnection, BIND_AUTO_CREATE);
    }

    private void start() {
        if (!pdService.isRunning()) {
            Intent intent = new Intent(Tuner.this, Tuner.class);
            pdService.startAudio(intent, org.puredata.android.service.R.drawable.icon, "GuitarTuner", "Return to GuitarTuner.");
        }
    }

    //
    private void initSystemServices() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                if (pdService == null) return;
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    start(); } else {
                    pdService.stopAudio(); }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void initPd() throws IOException{

        AudioParameters.init(this);
        // AudioParameters utility suggests available sample rates and channel numbers.
        int sampleRate = AudioParameters.suggestSampleRate();
        Log.e(TAG, sampleRate + "");
        pdService.initAudio(sampleRate, 1, 2, 10.0f);
        Log.e(TAG, "starting");
        start();

        // Create dispatcher.
        this.dispatcher = new PdUiDispatcher();
        PdBase.setReceiver(dispatcher);
        dispatcher.addListener("pitch", new PdListener.Adapter() {
            @Override
            public void receiveFloat(String source, final float x) {
                pitchView.setCurrentPitch(x);
            }
        });
    }

    private void initGui() {
        setContentView(R.layout.tuner);
        eButton = (Button) findViewById(R.id.e_button);
        eButton.setOnClickListener(this);
        aButton = (Button) findViewById(R.id.a_button);
        aButton.setOnClickListener(this);
        dButton = (Button) findViewById(R.id.d_button);
        dButton.setOnClickListener(this);
        gButton = (Button) findViewById(R.id.g_button);
        gButton.setOnClickListener(this);
        bButton = (Button) findViewById(R.id.b_button);
        bButton.setOnClickListener(this);
        eeButton = (Button) findViewById(R.id.ee_button);
        eeButton.setOnClickListener(this);
        pitchLabel = (TextView) findViewById(R.id.pitch_label);
        pitchView = (PitchView) findViewById(R.id.pitch_view);
        pitchView.setCenterPitch(45);
        pitchLabel.setText("A-String");
    }

    //
    private void loadPatch() throws IOException{
        Log.e( TAG, "Loading PD-patch.");
        File dir = getFilesDir();

        Log.e( TAG, "OpenRawResource.");
        InputStream zipResource = getResources().openRawResource(R.raw.tuner);
        Log.e( TAG, "extractZipResource.");

        IoUtils.extractZipResource( zipResource, dir, true);

        Log.e(TAG, "patchFile");
        File patchFile = new File(dir, "tuner.pd");
        PdBase.openPatch(patchFile.getAbsolutePath());
    }

    @Override
    protected void onResume(){
        super.onResume();
        PdAudio.startAudio(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        PdAudio.stopAudio();
    }


    private void triggerNote(int n) {
        Log.e( TAG, "triggernote.");
        PdBase.sendFloat("midinote", n);

        Log.e(TAG, n + "");
        PdBase.sendBang("trigger");
        pitchView.setCenterPitch(n);
    }

    @Override
    public void onClick(View v) {
        String buttonId = getResources().getResourceEntryName(v.getId());
        switch (buttonId) {
            case "e_button":
                triggerNote(40); // E (low) is MIDI note 40.
                pitchLabel.setText("E-String");
                break;
            case "a_button":
                triggerNote(45); // A is MIDI note 45.
                pitchLabel.setText("A-String");
                break;
            case "d_button":
                triggerNote(50); // D is MIDI note 50.
                pitchLabel.setText("D-String");
                break;
            case "g_button":
                triggerNote(55); // G is MIDI note 55.
                pitchLabel.setText("G-String");
                break;
            case "b_button":
                triggerNote(59); // B is MIDI note 59.
                pitchLabel.setText("B-String");
                break;
            case "ee_button":
                triggerNote(64); // E (high) is MIDI note 64.
                pitchLabel.setText("E-String");
                break;
        }
    }
}