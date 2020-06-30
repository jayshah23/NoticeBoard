package com.example.noticeboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NoticeEdit extends AppCompatActivity {
    DatabaseReference databaseReference;
    EditText etEditNoticeTitle, etEditNoticeSubject, etEditNoticeNotice;
    TextView tvEditNoticeUploadBy, tvEditNoticeType, tvEditNoticeCDate, tvEditNoticeDate, tvEditNoticeTime,
            tvEditNoticeSem, tvEditNoticeDept;
    Spinner spEditNoticeSem, spEditNoticeDept;
    ImageButton ibEditNoticeDate, ibEditNoticeTime;
    Button btnUpdateNotice;
    Calendar calendar;
    String ampm;
    Boolean timeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_edit);

        etEditNoticeTitle = findViewById(R.id.etEditNoticeTitle);
        etEditNoticeSubject = findViewById(R.id.etEditNoticeSubject);
        etEditNoticeNotice = findViewById(R.id.etEditNoticeNotice);
        tvEditNoticeUploadBy = findViewById(R.id.tvEditNoticeUploadBy);
        tvEditNoticeType = findViewById(R.id.tvEditNoticeType);
        tvEditNoticeCDate = findViewById(R.id.tvEditNoticeCDate);
        tvEditNoticeDate = findViewById(R.id.tvEditNoticeDate);
        tvEditNoticeTime = findViewById(R.id.tvEditNoticeTime);
        tvEditNoticeSem = findViewById(R.id.tvEditNoticeSem);
        tvEditNoticeDept = findViewById(R.id.tvEditNoticeDept);
        spEditNoticeSem = findViewById(R.id.spEditNoticeSem);
        spEditNoticeDept = findViewById(R.id.spEditNoticeDept);
        ibEditNoticeDate = findViewById(R.id.ibEditNoticeDate);
        ibEditNoticeTime = findViewById(R.id.ibEditNoticeTime);
        btnUpdateNotice = findViewById(R.id.btnUpdateNotice);

        calendar = Calendar.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("notice");
        final String key = getIntent().getStringExtra("key");

        databaseReference.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title,branch,sem,subject,notice,date,cdate,upload,time,type;
                title = dataSnapshot.child("title").getValue().toString();
                branch = dataSnapshot.child("branch").getValue().toString();
                sem = dataSnapshot.child("sem").getValue().toString();
                subject = dataSnapshot.child("subject").getValue().toString();
                notice = dataSnapshot.child("notice").getValue().toString();
                date = dataSnapshot.child("date").getValue().toString();
                cdate = dataSnapshot.child("cdate").getValue().toString();
                upload = dataSnapshot.child("upload").getValue().toString();
                time = dataSnapshot.child("time").getValue().toString();
                type = dataSnapshot.child("type").getValue().toString();

                etEditNoticeTitle.setText(title);
                etEditNoticeSubject.setText(subject);
                etEditNoticeNotice.setText(notice);
                tvEditNoticeUploadBy.setText(upload);
                tvEditNoticeType.setText(type);
                tvEditNoticeCDate.setText(cdate);
                tvEditNoticeDate.setText(date);
//                tvEditNoticeTime.setText(time);

//                if (!time.equals("")) {
//                    ibEditNoticeTime.setVisibility(View.VISIBLE);
//                    tvEditNoticeTime.setVisibility(View.VISIBLE);
//                    tvEditNoticeTime.setText(time);
//                }
//                else {
//                    tvEditNoticeTime.setVisibility(View.GONE);
//                    ibEditNoticeTime.setVisibility(View.GONE);
//                }

                if (time.equals("")) {
                    timeStatus = true;
                }
                else timeStatus = false;

                if (timeStatus) {
                    tvEditNoticeTime.setVisibility(View.GONE);
                    ibEditNoticeTime.setVisibility(View.GONE);
                }
                else {
                    ibEditNoticeTime.setVisibility(View.VISIBLE);
                    tvEditNoticeTime.setVisibility(View.VISIBLE);
                    tvEditNoticeTime.setText(time);
                }

                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(NoticeEdit.this,
                        R.array.department, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spEditNoticeDept.setAdapter(adapter1);
                int pos1 = adapter1.getPosition(branch);
                spEditNoticeDept.setSelection(pos1);

                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(NoticeEdit.this,
                        R.array.semester, android.R.layout.simple_spinner_item);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spEditNoticeSem.setAdapter(adapter2);
                int pos2 = adapter2.getPosition(sem);
                spEditNoticeSem.setSelection(pos2);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        ibEditNoticeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Year = calendar.get(Calendar.YEAR);
                int Month = calendar.get(Calendar.MONTH);
                int Day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(NoticeEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = DateFormat.getDateInstance().format(calendar.getTime());
                        tvEditNoticeDate.setText(date);
                    }
                }, Year, Month, Day);
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());
                datePickerDialog.show();
            }
        });

        ibEditNoticeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(NoticeEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            ampm = "PM";
                        }
                        else ampm = "AM";
                        if (hourOfDay > 12) {
                            hourOfDay -= 12;
                        }
                        tvEditNoticeTime.setText(String.format(String.format("%02d:%02d ", hourOfDay, minute) + ampm));
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        btnUpdateNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("title", etEditNoticeTitle.getText().toString());
                hashMap.put("subject", etEditNoticeSubject.getText().toString());
                hashMap.put("notice", etEditNoticeNotice.getText().toString());
                hashMap.put("sem", spEditNoticeSem.getSelectedItem().toString());
                hashMap.put("branch", spEditNoticeDept.getSelectedItem().toString());
                hashMap.put("date", tvEditNoticeDate.getText().toString());

                if (timeStatus) {
                    hashMap.put("time", "");
                }
                else hashMap.put("time", tvEditNoticeTime.getText().toString());

                databaseReference.child(key).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(NoticeEdit.this, "Notice Update Successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NoticeEdit.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
