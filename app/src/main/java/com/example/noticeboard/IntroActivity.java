package com.example.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class IntroActivity extends AppCompatActivity {

    ImageButton ibAdmin, ibStudent;
    FirebaseUser user;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        ibAdmin = findViewById(R.id.ibAdmin);
        ibStudent = findViewById(R.id.ibStudent);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String mail = "";
        String un = "";
        reference = FirebaseDatabase.getInstance().getReference("user");

        if (user != null) {
            ibAdmin.setVisibility(View.INVISIBLE);
            ibStudent.setVisibility(View.INVISIBLE);
            mail = user.getEmail();
            un = mail.replace(".", "_dot_");
            reference.child(un).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String typ = dataSnapshot.child("type").getValue().toString();
                    if (typ.equals("student")) {
                        Intent i = new Intent(IntroActivity.this, DashboardStudent.class);
                        startActivity(i);
                        finish();

                    }
                    if (typ.equals("admin")) {
                        Intent i = new Intent(IntroActivity.this, Dashboard.class);
                        startActivity(i);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(IntroActivity.this, "Error : "+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            ibAdmin.setVisibility(View.VISIBLE);
            ibStudent.setVisibility(View.VISIBLE);
        }

        ibStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IntroActivity.this, LoginStudent.class);
                startActivity(i);
            }
        });

        ibAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IntroActivity.this, AdminValidation.class);
                startActivity(i);
            }
        });
    }
}
