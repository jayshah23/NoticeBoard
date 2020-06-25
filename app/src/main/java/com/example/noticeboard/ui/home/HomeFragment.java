package com.example.noticeboard.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.noticeboard.NoticeAdapter;
import com.example.noticeboard.R;
import com.example.noticeboard.notice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    public HomeFragment(){} // an empty public constructor required
    DatabaseReference reference, useref;
    RecyclerView list_view;
    ArrayList<notice> itemlist;
    SearchView searchView_home;
    ImageView ivPopup_home;
    NoticeAdapter adapterClass;
    String sem;
    SwipeRefreshLayout refresh;
    FirebaseUser user;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        reference= FirebaseDatabase.getInstance().getReference().child("notice");
        reference.keepSynced(true);

        searchView_home = view.findViewById(R.id.SearchBar_home);
        ivPopup_home = view.findViewById(R.id.ivPopup_home);
        refresh = view.findViewById(R.id.refresh);
        user = FirebaseAuth.getInstance().getCurrentUser();

        list_view=view.findViewById(R.id.list_view);
        list_view.setHasFixedSize(true);
        list_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        ivPopup_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.setOnMenuItemClickListener(HomeFragment.this);
                popupMenu.inflate(R.menu.dashboard_filter);
                popupMenu.show();
            }
        });

        useref = FirebaseDatabase.getInstance().getReference("user");
        String em = FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".", "_dot_");
        useref.child(em).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sem = dataSnapshot.child("semester").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {
                            itemlist = new ArrayList<>();
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                notice n = child.getValue(notice.class);
                                itemlist.add(n);
                            }
                            adapterClass = new NoticeAdapter(itemlist);
                            list_view.setAdapter(adapterClass);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                refresh.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    itemlist = new ArrayList<>();
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        notice n = child.getValue(notice.class);
                        itemlist.add(n);
                    }
                    adapterClass = new NoticeAdapter(itemlist);
                    list_view.setAdapter(adapterClass);
                }

                if(searchView_home != null){
                    searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            search(newText);
                            return true;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void search(String s) {
        ArrayList<notice> myList = new ArrayList();
        for (notice object : itemlist) {
            if(object.getTitle().toLowerCase().contains(s.toLowerCase())) {
                myList.add(object);
            }
            if(object.getUpload().toLowerCase().contains(s.toLowerCase())) {
                myList.add(object);
            }
        }
        NoticeAdapter adapterClass = new NoticeAdapter(myList);
        list_view.setAdapter(adapterClass);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        ArrayList<notice> myList = new ArrayList();
        switch (item.getItemId()) {
            case R.id.it_home_New_Old :
                Collections.sort(itemlist, new Comparator<notice>() {
                    @Override
                    public int compare(notice o1, notice o2) {
                        String one = o1.getCdate().toLowerCase();
                        String two = o2.getCdate().toLowerCase();
                        return one.compareTo(two);
                    }
                });
                adapterClass.notifyDataSetChanged();
                break;

            case R.id.it_home_Old_New :
                Collections.sort(itemlist, new Comparator<notice>() {
                    @Override
                    public int compare(notice o1, notice o2) {
                        String one = o1.getCdate().toLowerCase();
                        String two = o2.getCdate().toLowerCase();
                        return two.compareTo(one);
                    }
                });
                adapterClass.notifyDataSetChanged();
                break;

            case R.id.it_home_MySem :
                myList.clear();
                for (notice object : itemlist) {
                    if(object.getSem().equals(sem)) {
                        myList.add(object);
                    }
                }
                NoticeAdapter adapterClass = new NoticeAdapter(myList);
                list_view.setAdapter(adapterClass);
                break;

            case R.id.it_home_MyNotice:
                myList.clear();
                for (notice object : itemlist) {
                    if(object.getUpload().equals(user.getEmail())) {
                        myList.add(object);
                    }
                }
                NoticeAdapter adapterClas = new NoticeAdapter(myList);
                list_view.setAdapter(adapterClas);
                break;
        }
        return true;
    }
}
