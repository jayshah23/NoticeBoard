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

public class LoginAdmin extends AppCompatActivity {

    EditText etSignInEmail, etSignInPassword;
    Button btnSignIn, btnSignUp, btnForgotPassword;
    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        etSignInEmail = findViewById(R.id.etSignInEmail);
        etSignInPassword = findViewById(R.id.etSignInPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("user");//

        if (user != null ) {
            Intent i = new Intent(LoginAdmin.this, Dashboard.class);
            startActivity(i);
            finish();
        }

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = etSignInEmail.getText().toString();
                final String password = etSignInPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginAdmin.this, "ENTER CREDENTIALS", Toast.LENGTH_SHORT).show();
                }
                else {
                    reference.orderByChild("email").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                String typ = dataSnapshot.child(username.replace(".", "_dot_")).child("type").getValue().toString();
                                if (typ.equals("admin")) {
                                    firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                startActivity(new Intent(LoginAdmin.this, Dashboard.class));
                                                etSignInEmail.setText("");
                                                etSignInPassword.setText("");
                                                etSignInEmail.requestFocus();
                                                finish();
                                            }
                                            else {
                                                Toast.makeText(LoginAdmin.this, "Failure \n" + task.getException(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginAdmin.this, "Failure : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(LoginAdmin.this, "Only admin's can access, "+typ+"'s cannot", Toast.LENGTH_SHORT).show();
                                    etSignInEmail.setText("");
                                    etSignInPassword.setText("");
                                    etSignInEmail.requestFocus();
                                    finish();
                                }
                            }
                            else {
                                Toast.makeText(LoginAdmin.this, "No such user exists", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {}
                    });
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginAdmin.this, AdminSignUpActivity.class);
                startActivity(i);
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginAdmin.this,ForgotPassword.class);
                startActivity(i);
            }
        });
    }
}
