package com.example.noticeboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NoticeSeminar extends AppCompatActivity {

    ImageButton ibSeminarDate, ibSeminarTime;
    TextView tvSeminarDate, tvSeminarDept, tvSeminarSem, tvSeminarTime, tvSeminarFile;
    EditText tvSeminarTitle, tvSeminarSubject, tvSeminarNotice;
    Spinner spSeminarSem, spSeminarDept;
    Calendar calendar;
    String ampm;
    DatabaseReference reference;
    Toolbar toolbar;
    Button btnSeminarFile;
    Switch switchSeminar;
    Uri uri;
    ProgressDialog progressDialog;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_seminar);

        ibSeminarDate = findViewById(R.id.ibSeminarDate);
        ibSeminarTime = findViewById(R.id.ibSeminarTime);
        tvSeminarDate = findViewById(R.id.tvSeminarDate);
        tvSeminarDept = findViewById(R.id.tvSeminarDept);
        tvSeminarSem = findViewById(R.id.tvSeminarSem);
        tvSeminarTime = findViewById(R.id.tvSeminarTime);
        tvSeminarTitle = findViewById(R.id.tvSeminarTitle);
        tvSeminarSubject = findViewById(R.id.tvSeminarSubject);
        tvSeminarNotice = findViewById(R.id.tvSeminarNotice);
        spSeminarSem = findViewById(R.id.spSeminarSem);
        spSeminarDept = findViewById(R.id.spSeminarDept);
        calendar = Calendar.getInstance();
        tvSeminarFile = findViewById(R.id.tvSeminarFile);
        btnSeminarFile = findViewById(R.id.btnSeminarFile);
        switchSeminar = findViewById(R.id.switchSeminar);
        reference = FirebaseDatabase.getInstance().getReference("notice");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("Seminar Notice");

        ibSeminarDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Year = calendar.get(Calendar.YEAR);
                int Month = calendar.get(Calendar.MONTH);
                int Day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(NoticeSeminar.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = DateFormat.getDateInstance().format(calendar.getTime());
                        tvSeminarDate.setText(date);
                    }
                }, Year, Month, Day);
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());
                datePickerDialog.show();
            }
        });

        ibSeminarTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(NoticeSeminar.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            ampm = "PM";
                        }
                        else ampm = "AM";
                        if (hourOfDay > 12) {
                            hourOfDay -= 12;
                        }
                        tvSeminarTime.setText(String.format(String.format("%02d:%02d ", hourOfDay, minute) + ampm));
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        switchSeminar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchSeminar.isChecked()) {
                    tvSeminarFile.setVisibility(View.VISIBLE);
                    btnSeminarFile.setVisibility(View.VISIBLE);
                }
                else {
                    tvSeminarFile.setVisibility(View.INVISIBLE);
                    btnSeminarFile.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnSeminarFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NoticeSeminar.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectFiles();
                }
                else ActivityCompat.requestPermissions(NoticeSeminar.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        String title = tvSeminarTitle.getText().toString();
//        String date = tvSeminarDate.getText().toString();
//        String time = tvSeminarTime.getText().toString();
//        String semester = spSeminarSem.getSelectedItem().toString();
//        String department = spSeminarDept.getSelectedItem().toString();
//        String subject = tvSeminarSubject.getText().toString();
//        String notice = tvSeminarNotice.getText().toString();
//
//        if (title.length() < 5) {
//            tvSeminarTitle.setError("Cannot be empty");
//            tvSeminarTitle.requestFocus();
//        }
//        if (date.equals("Select Date")) {
//            tvSeminarDate.setError("Select Date");
//        }
//        if (time.equals("Select Time")) {
//            tvSeminarTime.setError("Select Time");
//        }
//        if (notice.isEmpty()) {
//            tvSeminarNotice.setError("Cannot be empty");
//            tvSeminarNotice.requestFocus();
//        }
//        else if (item.getItemId() == R.id.itSent) {
//            if (!title.isEmpty() && !date.equals("Select Date") && !time.equals("Select Time") && !semester.equals("Select Semester")
//                    && !department.equals("Select Department") && !subject.isEmpty() && !notice.isEmpty()) {
//
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
//                String currentdate = sdf.format(new Date());
//                String upload = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//
//                notice n = new notice(title, department, semester, subject, notice, date, currentdate, upload, time, "Seminar Notice");
//                reference.push().setValue(n);
//                Toast.makeText(NoticeSeminar.this, "Notice added successfully", Toast.LENGTH_SHORT).show();
//
//                Intent i = new Intent(this, Dashboard.class);
//                startActivity(i);
//                finish();
//            }
//            else Toast.makeText(NoticeSeminar.this, "Enter all fields", Toast.LENGTH_SHORT).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final String title = tvSeminarTitle.getText().toString();
        final String date = tvSeminarDate.getText().toString();
        final String time = tvSeminarTime.getText().toString();
        final String semester = spSeminarSem.getSelectedItem().toString();
        final String department = spSeminarDept.getSelectedItem().toString();
        final String subject = tvSeminarSubject.getText().toString();
        final String notice = tvSeminarNotice.getText().toString();
        final String upload = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        final String current_date = sdf.format(new Date());
        final String filename = System.currentTimeMillis()+"";
        if (title.length() < 5) {
            tvSeminarTitle.setError("Cannot be empty");
            tvSeminarTitle.requestFocus();
        }
        if (date.equals("Select Date")) {
            tvSeminarDate.setError("Select Date");
        }
        if (notice.isEmpty()) {
            tvSeminarNotice.setError("Cannot be empty");
            tvSeminarNotice.requestFocus();
        }
        else if (item.getItemId() == R.id.itSent) {
            if (!title.isEmpty() && !date.equals("Select Date") && !semester.equals("Select Semester")
                    && !department.equals("Select Department") && !subject.isEmpty() && !notice.isEmpty() && uri != null) {

                progressDialog = new ProgressDialog(NoticeSeminar.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("Uploading....");
                progressDialog.setProgress(0);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                storageReference.child(filename).putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.child(filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                notice n = new notice(title, department, semester, subject, notice, date, current_date, upload, time, "Seminar Notice");String url = uri.toString();
                                reference.child(filename).setValue(n);
                                Toast.makeText(NoticeSeminar.this, "Done", Toast.LENGTH_SHORT).show();

                                reference.child(filename).child("files").setValue(url);
                                progressDialog.dismiss();

                                Intent i = new Intent(NoticeSeminar.this, Dashboard.class);
                                startActivity(i);
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NoticeSeminar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NoticeSeminar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        int CurrProgress = (int) ((100*taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount());
                        progressDialog.setProgress(CurrProgress);
                    }
                });

            }
            else {
                notice n = new notice(title, department, semester, subject, notice, date, current_date, upload, time, "Seminar Notice");
                reference.child(filename).setValue(n);
                Toast.makeText(NoticeSeminar.this, "Notice added successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, Dashboard.class);
                startActivity(i);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //File upload from here
    public void selectFiles() {
        Intent i = new Intent();
        i.setType("*/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 86);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectFiles();
        }
        else Toast.makeText(NoticeSeminar.this, "Please provide permissions...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 86 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            String dataaaa = data.getData().getLastPathSegment();
            if (dataaaa.contains("/")) {
                String name = dataaaa.substring(dataaaa.lastIndexOf("/")+1);
                tvSeminarFile.setText(name);
            }
            else tvSeminarFile.setText(dataaaa);
        }
        else Toast.makeText(NoticeSeminar.this, "Please select a file...", Toast.LENGTH_SHORT).show();
    }
}
