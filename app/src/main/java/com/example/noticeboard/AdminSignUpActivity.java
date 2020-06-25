package com.example.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminSignUpActivity extends AppCompatActivity {

    EditText etAdminName, etAdminIDNumber, etAdminPhoneNumber, etAdminEmail, etAdminPassword;
    Button btnAdminSignUp;
    Spinner spDepartment, spDesignation;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);

        etAdminName = findViewById(R.id.etAdminName);
        etAdminIDNumber = findViewById(R.id.etAdminIDNumber);
        etAdminPhoneNumber = findViewById(R.id.etAdminPhoneNumber);
        etAdminEmail = findViewById(R.id.etAdminEmail);
        etAdminPassword = findViewById(R.id.etAdminPassword);
        btnAdminSignUp = findViewById(R.id.btnAdminSignUp);
        spDepartment = findViewById(R.id.spDepartment);
        spDesignation = findViewById(R.id.spDesignation);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("user");//

        btnAdminSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name, ID, phone, email, password, department, designation;
                name = etAdminName.getText().toString();
                ID = etAdminIDNumber.getText().toString();
                phone = etAdminPhoneNumber.getText().toString();
                email = etAdminEmail.getText().toString();
                password = etAdminPassword.getText().toString();
                department = spDepartment.getSelectedItem().toString();
                designation = spDesignation.getSelectedItem().toString();
                String emailpattern = "[a-zA-Z0-9._-]+@somaiya.edu";
                final String key = email.replace(".", "_dot_");

                if (name.length() < 4){
                    etAdminName.setError("INVALID");
                }
                if (ID.length() != 10){
                    etAdminIDNumber.setError("INVALID");
                }
                if (phone.length() != 10){
                    etAdminPhoneNumber.setError("INVALID");
                }
                if (password.length() < 7){
                    etAdminPassword.setError("At-least 8 characters required");
                }
                if (department.equals("Select Department")){
                    spDepartment.requestFocus();
                }
                if (designation.equals("Select Designation")){
                    spDesignation.requestFocus();
                }
                if (email.length() == 0 || !email.matches(emailpattern)){
                    etAdminEmail.setError("Kindly use ...@somaiya.edu ID");
                }
                else {
                    reference.orderByChild("id_number").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                Toast.makeText(AdminSignUpActivity.this, "User with given ID card number already exists", Toast.LENGTH_LONG).show();
                                etAdminIDNumber.requestFocus();
                            }
                            else {
                                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            user u = new user(name, phone, department, email, "", "admin", designation, ID);
                                            reference.child(key).setValue(u);
                                            Intent i = new Intent(AdminSignUpActivity.this, Dashboard.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(AdminSignUpActivity.this, "Account creation failed:(" + "\n" + "Account with provided Email Id already exists"/*task.getException()*/, Toast.LENGTH_LONG).show();
                                            etAdminEmail.requestFocus();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
