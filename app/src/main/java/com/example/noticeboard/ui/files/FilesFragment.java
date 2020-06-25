package com.example.noticeboard.ui.files;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noticeboard.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FilesFragment extends Fragment {
    public FilesFragment() {}// Required empty public constructor
    RecyclerView recyclerView;
    TextView tvFileStatus;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_files, container, false);
        tvFileStatus = view.findViewById(R.id.tvFileStatus);

        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    tvFileStatus.setVisibility(View.GONE);
                    String filename = dataSnapshot.getKey();
                    String url = dataSnapshot.getValue(String.class);
                    ((MyAdapter)recyclerView.getAdapter()).update(filename, url);
                }
                else tvFileStatus.setVisibility(View.VISIBLE);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        MyAdapter myAdapter = new MyAdapter(recyclerView, getContext(), new ArrayList<String>(), new ArrayList<String>());

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(myAdapter);
        return view;
    }
}
