package com.example.noticeboard.ui.about;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.noticeboard.R;

public class AboutFragment extends Fragment {
    Button btnFeedback;
    ImageButton ibInstagram, ibFacebook, ibLinkIn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        btnFeedback = view.findViewById(R.id.btnFeedback);
        ibInstagram = view.findViewById(R.id.ibInstagram);
        ibFacebook = view.findViewById(R.id.ibFacebook);
        ibLinkIn = view.findViewById(R.id.ibLinkIn);

        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.google.com")));
            }
        });

        ibInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.instagram.com/it_kjsieit/")));
            }
        });

        ibFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.facebook.com/KJSIEITITDEPT")));
            }
        });

        ibLinkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.linkedin.com/school/kjsomaiya-institute-of-engineering-and-information-technology/")));
            }
        });
        return view;
    }
}