package com.example.noticeboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminValidation extends AppCompatActivity {
    EditText etValidationKey;
    Button btnValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_validation);
        etValidationKey = findViewById(R.id.etValidationKey);
        btnValidate = findViewById(R.id.btnValidate);

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etValidationKey.getText().toString().equals("admin")) {
                    startActivity(new Intent(AdminValidation.this, LoginAdmin.class));
                    etValidationKey.setText("");
                    finish();
                }
                else {
                    Toast.makeText(AdminValidation.this, "Invalid key", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AdminValidation.this, IntroActivity.class));
                }
            }
        });
    }
}
