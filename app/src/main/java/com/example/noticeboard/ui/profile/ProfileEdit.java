package com.example.noticeboard.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noticeboard.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileEdit extends AppCompatActivity {
    TextView tvEditType, tvEditEmail, tvEditID;
    EditText etEditName, etEditPhone;
    Spinner spEditSem, spEditDept, spEditDesg;
    Button btnUpdate;
    DatabaseReference reference;
    FirebaseUser user;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        tvEditType = findViewById(R.id.tvEditType);
        tvEditEmail = findViewById(R.id.tvEditEmail);
        tvEditID = findViewById(R.id.tvEditID);
        etEditName = findViewById(R.id.etEditName);
        etEditPhone = findViewById(R.id.etEditPhone);
        spEditSem = findViewById(R.id.spEditSem);
        spEditDept = findViewById(R.id.spEditDept);
        spEditDesg = findViewById(R.id.spEditDesg);
        btnUpdate = findViewById(R.id.btnUpdate);

        reference = FirebaseDatabase.getInstance().getReference("user");
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail().toString().replace(".", "_dot_");

        reference.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String type = dataSnapshot.child("type").getValue().toString().toUpperCase();
                String id_number = dataSnapshot.child("id_number").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String phone = dataSnapshot.child("phone").getValue().toString();
                String department = dataSnapshot.child("department").getValue().toString();
                String semester = dataSnapshot.child("semester").getValue().toString();
                String designation = dataSnapshot.child("designation").getValue().toString();

                tvEditType.setText(type);
                tvEditEmail.setText(email);
                tvEditID.setText(id_number);
                etEditName.setText(name);
                etEditPhone.setText(phone);

                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(ProfileEdit.this, R.array.department, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spEditDept.setAdapter(adapter1);
                int pos1 = adapter1.getPosition(department);
                spEditDept.setSelection(pos1);

                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(ProfileEdit.this, R.array.semester, android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spEditSem.setAdapter(adapter2);
                if (!semester.equals("")) {
                    int pos2 = adapter2.getPosition(semester);
                    spEditSem.setSelection(pos2);
                    spEditDesg.setVisibility(View.INVISIBLE);
                }
                else spEditDesg.setVisibility(View.VISIBLE);

                ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(ProfileEdit.this, R.array.designation, android.R.layout.simple_spinner_item);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spEditDesg.setAdapter(adapter3);
                if (!designation.equals("")) {
                    int pos3 = adapter3.getPosition(designation);
                    spEditDesg.setSelection(pos3);
                    spEditSem.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String new_name = etEditName.getText().toString();
                final String new_phone = etEditPhone.getText().toString();
                final String new_sem = spEditSem.getSelectedItem().toString();
                final String new_dept = spEditDept.getSelectedItem().toString();
                final String new_desg = spEditDesg.getSelectedItem().toString();
                final String old_type = tvEditType.getText().toString().toLowerCase();
                reference.child(email).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (old_type.equals("student")) {
                            dataSnapshot.getRef().child("name").setValue(new_name);
                            dataSnapshot.getRef().child("phone").setValue(new_phone);
                            dataSnapshot.getRef().child("semester").setValue(new_sem);
                            dataSnapshot.getRef().child("department").setValue(new_dept);
                            dataSnapshot.getRef().child("designation").setValue("");
                        }
                        else if (old_type.equals("admin")) {
                            dataSnapshot.getRef().child("name").setValue(new_name);
                            dataSnapshot.getRef().child("phone").setValue(new_phone);
                            dataSnapshot.getRef().child("semester").setValue("");
                            dataSnapshot.getRef().child("department").setValue(new_dept);
                            dataSnapshot.getRef().child("designation").setValue(new_desg);
                        }
                        Toast.makeText(ProfileEdit.this, "Update successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(ProfileEdit.this, "Update unsuccessful \n"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
}
