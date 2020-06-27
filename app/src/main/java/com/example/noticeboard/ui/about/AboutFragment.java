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

import com.example.noticeboard.R;

public class AboutFragment extends Fragment {
    Button btnSourceCode;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        btnSourceCode = view.findViewById(R.id.btnSourceCode);
        btnSourceCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setData(Uri.parse("https://github.com/jayshah23/24.04.2020.git"));
                startActivity(i);
            }
        });

        return view;
    }
}