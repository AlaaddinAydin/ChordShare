package com.example.chordshare.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chordshare.R;

public class AddEducation extends AppCompatActivity {

    private EditText editTextEducationName, editTextEducationDescription, editTextEducationLink;
    private String educationName, educationDescription, educationLink;
    private Button btnSave;
    public String linkId;

    private void init() {
        editTextEducationName = findViewById(R.id.add_activity_education_editTextEducationName);
        editTextEducationDescription = findViewById(R.id.add_activity_education_editTextEducationDescription);
        editTextEducationLink = findViewById(R.id.add_activity_editTextEducationLink);
        btnSave = findViewById(R.id.add_activity_Education_btnEducationSave);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_education);

        init();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EducationSave(v);
            }
        });
    }

    public void EducationSave(View v) {
        educationName = editTextEducationName.getText().toString();
        educationDescription = editTextEducationDescription.getText().toString();

        educationLink = getLinkId(editTextEducationLink.getText().toString());

        if (!TextUtils.isEmpty(educationName) && !TextUtils.isEmpty(educationDescription) && !TextUtils.isEmpty(educationLink)) {
            // Save to database
            try {
                SQLiteDatabase database = this.openOrCreateDatabase("Educations", MODE_PRIVATE, null);
                database.execSQL("CREATE TABLE IF NOT EXISTS educations (id INTEGER PRIMARY KEY, educationName VARCHAR, educationDescription VARCHAR, educationLink VARCHAR)");

                String sqlQuery = "INSERT INTO educations (educationName, educationDescription, educationLink) VALUES (?, ?, ?)";
                SQLiteStatement statement = database.compileStatement(sqlQuery);

                statement.bindString(1, educationName);
                statement.bindString(2, educationDescription);
                statement.bindString(3, educationLink);

                statement.execute();

                clearFields();
                showToast("Ekleme işlemi başarılı!");

            } catch (Exception e) {
                e.printStackTrace();
                showToast("Ekleme işlemi başarısız!");
            }

        } else {
            showToast("Lütfen tüm alanları doldurduğunuzdan emin olun.");
        }
    }

    private String getLinkId(String string) {
        int index = -1;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '=') {
                index = i;
                break;
            }
        }

        if (index != -1) { // "=" sembolü bulunduysa
            // İkinci parçayı alma
            linkId = string.substring(index + 1);
            return linkId;
        } else {
            showToast("Link'i doğru kopyaladığınızdan emin olun");
            return ""; // veya başka bir değer döndürülebilir
        }
    }


    private void clearFields() {
        editTextEducationName.setText("");
        editTextEducationDescription.setText("");
        editTextEducationLink.setText("");
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backIntent = new Intent(this, EducationMain.class);
        finish();
        startActivity(backIntent);
    }
}