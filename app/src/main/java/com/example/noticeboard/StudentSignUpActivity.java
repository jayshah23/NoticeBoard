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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentSignUpActivity extends AppCompatActivity {

    EditText etStudentName, etStudentIDNumber, etStudentPhoneNumber, etStudentEmail, etStudentPassword;
    Button btnStudentSignUp;
    Spinner spDepartment, spSemester;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign_up);

        etStudentName = findViewById(R.id.etStudentName);
        etStudentIDNumber = findViewById(R.id.etStudentIDNumber);
        etStudentPhoneNumber = findViewById(R.id.etStudentPhoneNumber);
        etStudentEmail = findViewById(R.id.etStudentEmail);
        etStudentPassword = findViewById(R.id.etStudentPassword);
        btnStudentSignUp = findViewById(R.id.btnStudentSignUp);
        spDepartment = findViewById(R.id.spDepartment);
        spSemester = findViewById(R.id.spSemester);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("user");//

        btnStudentSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name, ID, phone, email, password, department, semester;
                name = etStudentName.getText().toString();
                ID = etStudentIDNumber.getText().toString();
                phone = etStudentPhoneNumber.getText().toString();
                email = etStudentEmail.getText().toString();
                password = etStudentPassword.getText().toString();
                department = spDepartment.getSelectedItem().toString();
                semester = spSemester.getSelectedItem().toString();
                String emailpattern = "[a-zA-Z0-9._-]+@somaiya.edu";
                final String key = email.replace(".", "_dot_");

                if (name.length() < 4){
                    etStudentName.setError("INVALID");
                }
                if (ID.length() != 10){
                    etStudentIDNumber.setError("INVALID");
                }
                if (phone.length() != 10){
                    etStudentPhoneNumber.setError("INVALID");
                }
                if (password.length() < 7){
                    etStudentPassword.setError("At-least 8 characters required");
                }
                if (department.equals("Select Department")){
                    spDepartment.requestFocus();
                }
                if (semester.equals("Select Semester")){
                    spSemester.requestFocus();
                }
                if (email.length() == 0 || !email.matches(emailpattern)){
                    etStudentEmail.setError("Kindly use ...@somaiya.edu ID");
                }
                else {
                    reference.orderByChild("id_number").equalTo(ID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null){
                                Toast.makeText(StudentSignUpActivity.this, "User with given ID card number already exists", Toast.LENGTH_LONG).show();
                                etStudentIDNumber.requestFocus();
                            }
                            else {
                                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            user u = new user(name, phone, department, email, semester, "student", "", ID);
                                            reference.child(key).setValue(u);
                                            Intent i = new Intent(StudentSignUpActivity.this, DashboardStudent.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(StudentSignUpActivity.this, "Account creation failed:(" + "\n" + "Account with provided Email Id already exists"/*task.getException()*/, Toast.LENGTH_LONG).show();
                                            etStudentEmail.requestFocus();
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
