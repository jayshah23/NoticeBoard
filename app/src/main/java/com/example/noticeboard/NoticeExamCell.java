package com.example.noticeboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

public class NoticeExamCell extends AppCompatActivity {

    ImageButton ibExamDate;
    TextView tvExamDate, tvExamDept, tvExamSem, tvExamFile;
    EditText tvExamTitle, tvExamSubject, tvExamNotice;
    Spinner spExamSem, spExamDept;
    Calendar calendar;
    DatabaseReference reference;
    Toolbar toolbar;
    Button btnExamFile;
    Switch switchExam;
    Uri uri;
    ProgressDialog progressDialog;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_exam_cell);

        ibExamDate = findViewById(R.id.ibExamDate);
        tvExamDate = findViewById(R.id.tvExamDate);
        tvExamDept = findViewById(R.id.tvExamDept);
        tvExamSem = findViewById(R.id.tvExamSem);
        tvExamTitle = findViewById(R.id.tvExamTitle);
        tvExamSubject = findViewById(R.id.tvExamSubject);
        tvExamNotice = findViewById(R.id.tvExamNotice);
        spExamSem = findViewById(R.id.spExamSem);
        spExamDept = findViewById(R.id.spExamDept);
        calendar = Calendar.getInstance();
        tvExamFile = findViewById(R.id.tvExamFile);
        btnExamFile = findViewById(R.id.btnExamFile);
        switchExam = findViewById(R.id.switchExam);
        reference = FirebaseDatabase.getInstance().getReference("notice");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("ExamCell Notice");

        ibExamDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Year = calendar.get(Calendar.YEAR);
                int Month = calendar.get(Calendar.MONTH);
                int Day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(NoticeExamCell.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = DateFormat.getDateInstance().format(calendar.getTime());
                        tvExamDate.setText(date);
                    }
                }, Year, Month, Day);
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());
                datePickerDialog.show();
            }
        });

        switchExam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchExam.isChecked()) {
                    tvExamFile.setVisibility(View.VISIBLE);
                    btnExamFile.setVisibility(View.VISIBLE);
                }
                else {
                    tvExamFile.setVisibility(View.INVISIBLE);
                    btnExamFile.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnExamFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NoticeExamCell.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectFiles();
                }
                else ActivityCompat.requestPermissions(NoticeExamCell.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
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
//        String title = tvExamTitle.getText().toString();
//        String date = tvExamDate.getText().toString();
//        String semester = spExamSem.getSelectedItem().toString();
//        String department = spExamDept.getSelectedItem().toString();
//        String subject = tvExamSubject.getText().toString();
//        String notice = tvExamNotice.getText().toString();
//
//        if (title.length() < 5) {
//            tvExamTitle.setError("Cannot be empty");
//            tvExamTitle.requestFocus();
//        }
//        if (date.equals("Select Date")) {
//            tvExamDate.setError("Select Date");
//        }
//        if (notice.isEmpty()) {
//            tvExamNotice.setError("Cannot be empty");
//            tvExamNotice.requestFocus();
//        }
//        else if (item.getItemId() == R.id.itSent) {
//            if (!title.isEmpty() && !date.equals("Select Date") && !semester.equals("Select Semester")
//                    && !department.equals("Select Department") && !subject.isEmpty() && !notice.isEmpty()) {
//
//                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
//                String currentdate = sdf.format(new Date());
//                String upload = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//
//                notice n = new notice(title, department, semester, subject, notice, date, currentdate, upload, "", "ExamCell Notice");
//                reference.push().setValue(n);
//                Toast.makeText(NoticeExamCell.this, "Notice added successfully", Toast.LENGTH_SHORT).show();
//
//                Intent i = new Intent(this, Dashboard.class);
//                startActivity(i);
//                finish();
//            }
//            else Toast.makeText(NoticeExamCell.this, "Enter all fields", Toast.LENGTH_SHORT).show();
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final String title = tvExamTitle.getText().toString();
        final String date = tvExamDate.getText().toString();
        final String semester = spExamSem.getSelectedItem().toString();
        final String department = spExamDept.getSelectedItem().toString();
        final String subject = tvExamSubject.getText().toString();
        final String notice = tvExamNotice.getText().toString();
        final String upload = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        final String current_date = sdf.format(new Date());
        final String filename = System.currentTimeMillis()+"";
        if (title.length() < 5) {
            tvExamTitle.setError("Cannot be empty");
            tvExamTitle.requestFocus();
        }
        if (date.equals("Select Date")) {
            tvExamDate.setError("Select Date");
        }
        if (notice.isEmpty()) {
            tvExamNotice.setError("Cannot be empty");
            tvExamNotice.requestFocus();
        }
        else if (item.getItemId() == R.id.itSent) {
            if (!title.isEmpty() && !date.equals("Select Date") && !semester.equals("Select Semester")
                    && !department.equals("Select Department") && !subject.isEmpty() && !notice.isEmpty() && uri != null) {

                progressDialog = new ProgressDialog(NoticeExamCell.this);
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

                                notice n = new notice(title, department, semester, subject, notice, date, current_date,
                                        upload, "", "Department Notice", filename);
                                String url = uri.toString();
                                reference.child(filename).setValue(n);
                                Toast.makeText(NoticeExamCell.this, "Done", Toast.LENGTH_SHORT).show();

                                reference.child(filename).child("files").setValue(url);
                                progressDialog.dismiss();

                                Intent i = new Intent(NoticeExamCell.this, Dashboard.class);
                                startActivity(i);
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NoticeExamCell.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NoticeExamCell.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                notice n = new notice(title, department, semester, subject, notice, date, current_date,
                        upload, "", "Department Notice", filename);
                reference.child(filename).setValue(n);
                Toast.makeText(NoticeExamCell.this, "Notice added successfully", Toast.LENGTH_SHORT).show();
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
        else Toast.makeText(NoticeExamCell.this, "Please provide permissions...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 86 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            String dataaaa = data.getData().getLastPathSegment();
            if (dataaaa.contains("/")) {
                String name = dataaaa.substring(dataaaa.lastIndexOf("/")+1);
                tvExamFile.setText(name);
            }
            else tvExamFile.setText(dataaaa);
        }
        else Toast.makeText(NoticeExamCell.this, "Please select a file...", Toast.LENGTH_SHORT).show();
    }
}
