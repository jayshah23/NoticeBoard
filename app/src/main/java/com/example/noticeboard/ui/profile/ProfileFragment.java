package com.example.noticeboard.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.noticeboard.Dashboard;
import com.example.noticeboard.R;
import com.example.noticeboard.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView tvProfileType,tvProfileID ,tvProfileEmail, tvProfileName, tvProfilePhNum, tvProfileDept,
            tvProfileSem, tvProfileDesg, tvProfileVerify;
    ImageButton ibEdit;
    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvProfileType = view.findViewById(R.id.tvProfileType);
        tvProfileID = view.findViewById(R.id.tvProfileID);
        tvProfileEmail = view.findViewById(R.id.tvProfileEmail);
        tvProfileName = view.findViewById(R.id.tvProfileName);
        tvProfilePhNum = view.findViewById(R.id.tvProfilePhNum);
        tvProfileDept = view.findViewById(R.id.tvProfileDept);
        tvProfileSem = view.findViewById(R.id.tvProfileSem);
        tvProfileDesg = view.findViewById(R.id.tvProfileDesg);
        tvProfileVerify = view.findViewById(R.id.tvProfileVerify);
        ibEdit = view.findViewById(R.id.ibEdit);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("user");
        user = FirebaseAuth.getInstance().getCurrentUser();

        String acc = user.getEmail().replace(".", "_dot_");
        reference.child(acc).addValueEventListener(new ValueEventListener() {
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

//                String cut1 = id_number.substring(0,7).replaceAll("[0-9]", "*");
//                String cut2 = id_number.substring(7);
                tvProfileType.setText(type);
                tvProfileID.setText("".concat(id_number.substring(0,7).replaceAll("[0-9]", "*")).concat(id_number.substring(7)));
                tvProfileEmail.setText(email);
                tvProfileName.setText("".concat("Name : "+name));
                tvProfilePhNum.setText("".concat("Contact Number : ").concat(phone.substring(0,7).replaceAll("[0-9]", "*")).concat(phone.substring(7)));
                tvProfileDept.setText("".concat("Department : "+department));
                if (semester.equals("")) {
                    tvProfileSem.setVisibility(View.GONE);
                    tvProfileDesg.setText("".concat("Designation : "+designation));
                }
                if (designation.equals("")) {
                    tvProfileDesg.setVisibility(View.GONE);
                    tvProfileSem.setText("".concat("Semester : "+semester));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ProfileEdit.class);
                startActivity(i);
            }
        });

        if (!user.isEmailVerified()) {
            tvProfileVerify.setText("Click here to verify your Email ID");
            tvProfileVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(), "Email verification link sent to "+user.getEmail(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Email verification failed "+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
        else tvProfileVerify.setText("Verified Account ✔");

        return view;
    }
}
