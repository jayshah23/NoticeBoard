package com.example.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NoticeDetails extends AppCompatActivity {

    TextView tvDetailTitle, tvDetailUploadBy, tvDetailDate, tvDetailDept, tvDetailSem, tvDetailSubject, tvDetailNotice,
            tvDetailTime, tvDetailLastDate, tvDetailFile;
    String title, department, semester, subject, notice, date, current_date, upload, time, key;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);

        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailUploadBy = findViewById(R.id.tvDetailUploadBy);
        tvDetailDate = findViewById(R.id.tvDetailDate);
        tvDetailDept = findViewById(R.id.tvDetailDept);
        tvDetailSem = findViewById(R.id.tvDetailSem);
        tvDetailSubject = findViewById(R.id.tvDetailSubject);
        tvDetailNotice = findViewById(R.id.tvDetailNotice);
        tvDetailTime = findViewById(R.id.tvDetailTime);
        tvDetailLastDate = findViewById(R.id.tvDetailLastDate);
        tvDetailFile = findViewById(R.id.tvDetailFile);
        reference = FirebaseDatabase.getInstance().getReference().child("notice");

        Intent i = getIntent();
        title = i.getStringExtra("title");
        department = i.getStringExtra("dept");
        semester = i.getStringExtra("sem");
        subject = i.getStringExtra("subject");
        notice = i.getStringExtra("notice");
        date = i.getStringExtra("date");
        current_date = i.getStringExtra("currdate");
        upload = i.getStringExtra("upload");
        time = i.getStringExtra("time");
        key = i.getStringExtra("key");

        tvDetailTitle.setText(title);
        tvDetailUploadBy.setText(upload);
        tvDetailDate.setText("Uploaded on :"+current_date);
        tvDetailDept.setText(department);
        tvDetailSem.setText(semester);
        tvDetailSubject.setText(subject);
        tvDetailNotice.setText(notice);
        tvDetailTime.setText("Time : "+time);
        tvDetailLastDate.setText("Last Date : "+date);

        reference.child(key).addValueEventListener(new ValueEventListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("files")) {
                    final String file_url = dataSnapshot.child("files").getValue().toString();
                    tvDetailFile.setText(file_url);
                    tvDetailFile.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            Intent i = new Intent();
                            i.setDataAndType(Uri.parse(file_url), Intent.ACTION_VIEW);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            return true;
                        }
                    });
                }
                else tvDetailFile.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.itShare) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/*");
            i.putExtra(Intent.EXTRA_SUBJECT, subject);
            i.putExtra(Intent.EXTRA_TEXT, title+"\nfor "+department+"\n"+semester+"\n"+current_date+"\n"+notice+"\nLast date : "+date+"\n"+time);
            startActivity(Intent.createChooser(i,"Share using..."));
        }
        return super.onOptionsItemSelected(item);
    }
}
