package com.example.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginStudent extends AppCompatActivity {

    EditText etStudentSignInEmail, etStudentSignInPassword;
    Button btnStudentSignIn, btnStudentSignUp, btnStudentForgotPassword;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_student);

        etStudentSignInEmail = findViewById(R.id.etStudentSignInEmail);
        etStudentSignInPassword = findViewById(R.id.etStudentSignInPassword);
        btnStudentSignIn = findViewById(R.id.btnStudentSignIn);
        btnStudentSignUp = findViewById(R.id.btnStudentSignUp);
        btnStudentForgotPassword = findViewById(R.id.btnStudentForgotPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("user");//

        if (user != null ) {
            Intent i = new Intent(LoginStudent.this, DashboardStudent.class);
            startActivity(i);
            finish();
        }

        btnStudentSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etStudentSignInEmail.getText().toString();
                final String password = etStudentSignInPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginStudent.this, "ENTER CREDENTIALS", Toast.LENGTH_SHORT).show();
                }
                else {
                    reference.orderByChild("email").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                String typ = dataSnapshot.child(username.replace(".", "_dot_")).child("type").getValue().toString();
                                if (typ.equals("student")) {
                                    firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(LoginStudent.this, DashboardStudent.class));
                                                etStudentSignInEmail.setText("");
                                                etStudentSignInPassword.setText("");
                                                etStudentSignInEmail.requestFocus();
                                                finish();
                                            }
                                            else {
                                                Toast.makeText(LoginStudent.this, "Failure \n" + task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginStudent.this, "Failure : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(LoginStudent.this, "Only student's can access, "+typ+"'s cannot", Toast.LENGTH_SHORT).show();
                                    etStudentSignInEmail.setText("");
                                    etStudentSignInPassword.setText("");
                                    etStudentSignInEmail.requestFocus();
                                    finish();
                                }
                            }
                            else {
                                Toast.makeText(LoginStudent.this, "No such user exists", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }
        });

        btnStudentSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginStudent.this, StudentSignUpActivity.class);
                startActivity(i);
            }
        });

        btnStudentForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginStudent.this,ForgotPassword.class);
                startActivity(i);
            }
        });
    }
}
