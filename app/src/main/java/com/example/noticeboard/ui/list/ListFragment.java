package com.example.noticeboard.ui.list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.noticeboard.R;
import com.example.noticeboard.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ListFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    public ListFragment() {}// Required empty public constructor
    ListView lvUsers;
    ArrayList<String> users_list = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    DatabaseReference reference;
    SearchView SearchBar_users;
    ImageView ivPopup_users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);

        lvUsers = view.findViewById(R.id.lvUsers);
        SearchBar_users = view.findViewById(R.id.SearchBar_users);
        ivPopup_users = view.findViewById(R.id.ivPopup_users);

        reference = FirebaseDatabase.getInstance().getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String email = ds.child("email").getValue().toString();
                    String type = ds.child("type").getValue().toString();
                    users_list.add(email+"\t\t\t\t"+type);
                }
                arrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1, users_list);
                lvUsers.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ivPopup_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.setOnMenuItemClickListener(ListFragment.this);
                popupMenu.inflate(R.menu.user_filter);
                popupMenu.show();
            }
        });

        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                final String email = parent.getItemAtPosition(position).toString().substring(0, parent.getItemAtPosition(position).toString().indexOf("u")+1);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Do you want to E-mail "+email);
                builder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:"+email)));
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(SearchBar_users != null){
            SearchBar_users.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String abc : users_list) {
                        if (abc.contains(newText.toLowerCase())) {
                            list.add(abc);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
                        lvUsers.setAdapter(adapter);
                    }
                    return true;
                }
            });
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_user_admins:
                list.clear();
                for (String admin : users_list) {
                    if (admin.contains("admin")) {
                        list.add(admin);
                    }
                }
                ArrayAdapter<String> A_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
                lvUsers.setAdapter(A_adapter);
                break;

            case R.id.it_user_students:
                list.clear();
                for (String student : users_list) {
                    if (student.contains("student")) {
                        list.add(student);
                    }
                }
                ArrayAdapter<String> S_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
                lvUsers.setAdapter(S_adapter);
                break;

            case R.id.it_user_all:
                list.clear();
                for (String all : users_list) {
                    if (all.contains("student") || all.contains("admin")) {
                        list.add(all);
                    }
                }
                ArrayAdapter<String> All_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
                lvUsers.setAdapter(All_adapter);
        }
        return true;
    }
}
