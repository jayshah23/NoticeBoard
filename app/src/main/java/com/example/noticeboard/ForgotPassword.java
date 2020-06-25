package com.example.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class ForgotPassword extends AppCompatActivity {
    TextView tvStatus;
    Button btnSend;
    EditText etEmail;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        tvStatus = findViewById(R.id.tvStatus);
        btnSend = findViewById(R.id.btnSend);
        etEmail = findViewById(R.id.etEmail);
        auth = FirebaseAuth.getInstance();

        final String pattern = "[a-zA-Z0-9._-]+@somaiya.edu";
        final String email = etEmail.getText().toString();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.matches(pattern)){
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this, "Reset link send to email", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ForgotPassword.this, IntroActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else {
                                Toast.makeText(ForgotPassword.this, "Failure :( \n"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    tvStatus.setText("Invalid Email ID \nPlease enter ...@somaiya.edu ID");
                    etEmail.requestFocus();
                    etEmail.setText("");
                }
            }
        });
    }
}
