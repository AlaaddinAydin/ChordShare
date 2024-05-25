package com.example.chordshare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chordshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {


    EditText txtLUsername,txtLPassword;

    Button btnLLogin;
    private Vibrator v;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txtLUsername = (EditText) findViewById(R.id.txtLUsername);
        txtLPassword = (EditText) findViewById(R.id.txtLPassword);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnLLogin = (Button) findViewById(R.id.btnLLogin);

        databaseReference = FirebaseDatabase.getInstance().getReference("user");


        btnLLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = txtLUsername.getText().toString().trim();
                final String password = txtLPassword.getText().toString().trim();

                // Kullanıcı adı ve şifre boş kontrolü
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Lütfen kullanıcı adı ve şifreyi girin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kullanıcı adıyla eşleşen verileri Firebase'den al
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Kullanıcı adı var mı kontrol et
                        if (dataSnapshot.child("username").exists()) {
                            // Şifre kontrolü
                            if (dataSnapshot.child("username").getValue().toString().equals(username)
                                    && dataSnapshot.child("userpass").getValue().toString().equals(password)) {
                                // Giriş başarılı
                                // Go to Main
                                v.vibrate(1000);

                                Intent home = new Intent(Login.this , Buttons.class);
                                startActivity(home);
                            } else {
                                // Şifre yanlış
                                Toast.makeText(Login.this, "Hatalı kullanıcı adı veya şifre", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Kullanıcı adı yok
                            Toast.makeText(Login.this, "Bu kullanıcı adı bulunamadı", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Hata durumu
                        Toast.makeText(Login.this, "Hata: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });





    }
}