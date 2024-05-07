package com.example.chordshare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chordshare.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Register extends AppCompatActivity {



    Button btnRRegister,btnRDate;

    EditText txtRUsername,txtRPassword,txtRPassRep;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int year, month, dayOfMonth;
    private Vibrator v;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("user");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        btnRRegister = (Button) findViewById(R.id.btnRRegister);
        btnRDate = (Button) findViewById(R.id.btnRDate);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        txtRUsername = (EditText) findViewById(R.id.txtRUsername);
        txtRPassword = (EditText) findViewById(R.id.txtRPassword);
        txtRPassRep = (EditText) findViewById(R.id.txtRPassRep);


        btnRDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(Register.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                btnRDate.setText(day + "/" + (month+1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });


        btnRRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtRPassRep.getText().toString().equals("") ||
                        txtRPassword.getText().toString().equals("") ||
                        txtRUsername.getText().toString().equals("") ||
                        btnRDate.getText().toString().equals("GG.AA.YYYY") ||
                        !(txtRPassword.getText().toString().equals(txtRPassRep.getText().toString())))
                {
                    Toast.makeText(getApplicationContext(),"Boşlukları doldurduğunuzdan, tarihi ayarladığınızdan ve parolanızı doğruladığınızdan emin olun",Toast.LENGTH_LONG).show();
                }
                else {
                    myRef.child("username").setValue(txtRUsername.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Kayıt başarılı", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Kayıt başarısız: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                    myRef.child("userpass").setValue(txtRPassword.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Kayıt başarılı", Toast.LENGTH_LONG).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Kayıt başarısız: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                    myRef.child("userdate").setValue(btnRDate.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);

                                    // ProgressBar için LinearLayout oluştur
                                    LinearLayout layout = new LinearLayout(Register.this);
                                    layout.setOrientation(LinearLayout.VERTICAL);
                                    layout.setGravity(Gravity.CENTER);

                                    // ProgressBar oluştur ve ayarla
                                    ProgressBar progressBar = new ProgressBar(Register.this);
                                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                                    );
                                    progressBar.setLayoutParams(layoutParams);
                                    layout.addView(progressBar);

                                    // TextView oluştur ve ayarla
                                    TextView textView = new TextView(Register.this);
                                    textView.setText("Kayıt işleminiz sürüyor, lütfen bekleyiniz...");
                                    textView.setGravity(Gravity.CENTER);
                                    layout.addView(textView);

                                    // LinearLayout'ı AlertDialog'a ekle
                                    builder.setView(layout);

                                    // AlertDialog'u göster
                                    AlertDialog alertDialog = builder.show();
                                    v.vibrate(1000);

                                    // CountDownTimer oluştur ve 3 saniye beklet
                                    new CountDownTimer(3000, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            // Her saniye bir işlem yapılabilir, ancak burada bir şey yapmıyoruz
                                        }

                                        public void onFinish() {
                                            // AlertDialog'u kapat
                                            alertDialog.dismiss();
                                            // Login aktivitesine geçiş yap
                                            Intent intent = new Intent(Register.this, Login.class);
                                            startActivity(intent);
                                        }
                                    }.start();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Kayıt başarısız: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });


                }
            }
        });



    }
}