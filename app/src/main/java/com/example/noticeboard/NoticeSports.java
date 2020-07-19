package com.example.noticeboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class NoticeSports extends AppCompatActivity {
    ImageButton ibSportsDate, ibSportsTime;
    TextView tvSportsDate, tvSportsDept, tvSportsSem, tvSportsTime, tvSportsFile, tvSportsSemData, tvSportsDeptData;
    EditText tvSportsTitle, tvSportsSubject, tvSportsNotice;
    Calendar calendar;
    String ampm;
    DatabaseReference reference;
    Toolbar toolbar;
    Button btnSportsFile;
    Switch switchSports;
    Uri uri;
    ProgressDialog progressDialog;
    StorageReference storageReference;
    AlertDialog.Builder builder;
    CheckBox cb_semI, cb_semII, cb_semIII, cb_semIV, cb_semV, cb_semVI, cb_semVII, cb_semVIII, cb_CS, cb_IT, cb_EXTC, cb_ETRX, cb_AI_DS;
    StringBuilder data = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_sports);

        ibSportsDate = findViewById(R.id.ibSportsDate);
        ibSportsTime = findViewById(R.id.ibSportsTime);
        tvSportsDate = findViewById(R.id.tvSportsDate);
        tvSportsDept = findViewById(R.id.tvSportsDept);
        tvSportsSem = findViewById(R.id.tvSportsSem);
        tvSportsTime = findViewById(R.id.tvSportsTime);
        tvSportsTitle = findViewById(R.id.tvSportsTitle);
        tvSportsSubject = findViewById(R.id.tvSportsSubject);
        tvSportsNotice = findViewById(R.id.tvSportsNotice);
        tvSportsSemData = findViewById(R.id.tvSportsSemData);
        tvSportsDeptData = findViewById(R.id.tvSportsDeptData);
        calendar = Calendar.getInstance();
        tvSportsFile = findViewById(R.id.tvSportsFile);
        btnSportsFile = findViewById(R.id.btnSportsFile);
        switchSports = findViewById(R.id.switchSports);
        reference = FirebaseDatabase.getInstance().getReference("notice");
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        builder = new AlertDialog.Builder(this);

        toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("Sports Notice");

        ibSportsDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Year = calendar.get(Calendar.YEAR);
                int Month = calendar.get(Calendar.MONTH);
                int Day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(NoticeSports.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = DateFormat.getDateInstance().format(calendar.getTime());
                        tvSportsDate.setText(date);
                    }
                }, Year, Month, Day);
                datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTime().getTime());
                datePickerDialog.show();
            }
        });

        ibSportsTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(NoticeSports.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            ampm = "PM";
                        }
                        else ampm = "AM";
                        if (hourOfDay > 12) {
                            hourOfDay -= 12;
                        }
                        tvSportsTime.setText(String.format(String.format("%02d:%02d ", hourOfDay, minute) + ampm));
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        switchSports.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switchSports.isChecked()) {
                    tvSportsFile.setVisibility(View.VISIBLE);
                    btnSportsFile.setVisibility(View.VISIBLE);
                }
                else {
                    tvSportsFile.setVisibility(View.INVISIBLE);
                    btnSportsFile.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnSportsFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NoticeSports.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    selectFiles();
                }
                else ActivityCompat.requestPermissions(NoticeSports.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
            }
        });

        tvSportsSem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(NoticeSports.this).inflate(R.layout.dialog_box_semester, null);
                cb_semI = view.findViewById(R.id.cb_semI);
                cb_semII = view.findViewById(R.id.cb_semII);
                cb_semIII = view.findViewById(R.id.cb_semIII);
                cb_semIV = view.findViewById(R.id.cb_semIV);
                cb_semV = view.findViewById(R.id.cb_semV);
                cb_semVI = view.findViewById(R.id.cb_semVI);
                cb_semVII = view.findViewById(R.id.cb_semVII);
                cb_semVIII = view.findViewById(R.id.cb_semVIII);
                builder.setView(view);
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.delete(0, data.length()).append("Sem");
                        if (cb_semI.isChecked()) { data.append(" 1,"); }
                        if (cb_semII.isChecked()) { data.append(" 2,"); }
                        if (cb_semIII.isChecked()) { data.append(" 3,"); }
                        if (cb_semIV.isChecked()) { data.append(" 4,"); }
                        if (cb_semV.isChecked()) { data.append(" 5,"); }
                        if (cb_semVI.isChecked()) { data.append(" 6,"); }
                        if (cb_semVII.isChecked()) { data.append(" 7,"); }
                        if (cb_semVIII.isChecked()) { data.append(" 8"); }
                        if (!cb_semI.isChecked() && !cb_semII.isChecked() && !cb_semIII.isChecked() && !cb_semIV.isChecked() && !cb_semV.isChecked() && !cb_semVI.isChecked() && !cb_semVII.isChecked() && !cb_semVIII.isChecked()) {
                            Toast.makeText(view.getContext(), "Please select at-least one semester", Toast.LENGTH_SHORT).show();
                        }
                        else tvSportsSemData.setText(data);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

        tvSportsDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = LayoutInflater.from(NoticeSports.this).inflate(R.layout.dialog_box_department, null);
                cb_CS = view.findViewById(R.id.cb_CS);
                cb_IT = view.findViewById(R.id.cb_IT);
                cb_EXTC = view.findViewById(R.id.cb_EXTC);
                cb_ETRX = view.findViewById(R.id.cb_ETRX);
                cb_AI_DS = view.findViewById(R.id.cb_AI_DS);
                builder.setView(view);
                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        data.delete(0, data.length());
                        if (cb_CS.isChecked()) { data.append(" CS,"); }
                        if (cb_IT.isChecked()) { data.append(" IT,"); }
                        if (cb_EXTC.isChecked()) { data.append(" EXTC,"); }
                        if (cb_ETRX.isChecked()) { data.append(" ETRX,"); }
                        if (cb_AI_DS.isChecked()) { data.append(" AI-DS"); }
                        if (!cb_CS.isChecked() && !cb_IT.isChecked() && !cb_EXTC.isChecked() && !cb_ETRX.isChecked() && !cb_AI_DS.isChecked()) {
                            Toast.makeText(view.getContext(), "Please select at-least one department", Toast.LENGTH_SHORT).show();
                        }
                        else tvSportsDeptData.setText(data);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final String title = tvSportsTitle.getText().toString();
        final String date = tvSportsDate.getText().toString();
        final String time = tvSportsTime.getText().toString();
        final String semester = tvSportsSemData.getText().toString();
        final String department = tvSportsDeptData.getText().toString();
        final String subject = tvSportsSubject.getText().toString();
        final String notice = tvSportsNotice.getText().toString();

        final String upload = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        final String current_date = sdf.format(new Date());
        final String filename = System.currentTimeMillis()+"";
        if (title.isEmpty()) {
            tvSportsTitle.setError("Cannot be empty");
            tvSportsTitle.requestFocus();
        }
        if (date.equals("Select Date")) {
            tvSportsDate.setError("Select Date");
        }
        if (notice.isEmpty()) {
            tvSportsNotice.setError("Cannot be empty");
            tvSportsNotice.requestFocus();
        }
        else if (item.getItemId() == R.id.itSent) {
            if (!title.isEmpty() && !date.equals("Select Date") && !semester.isEmpty()
                    && !department.equals("Select Department") && !subject.isEmpty() && !notice.isEmpty() && uri != null) {

                progressDialog = new ProgressDialog(NoticeSports.this);
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
                                        upload, time, "Sports Notice", filename);
                                String url = uri.toString();
                                reference.child(filename).setValue(n);
                                Toast.makeText(NoticeSports.this, "Done", Toast.LENGTH_SHORT).show();

                                reference.child(filename).child("files").setValue(url);
                                progressDialog.dismiss();

                                Intent i = new Intent(NoticeSports.this, Dashboard.class);
                                startActivity(i);
                                finish();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NoticeSports.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NoticeSports.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        upload, time, "Sports Notice", filename);
                reference.child(filename).setValue(n);
                Toast.makeText(NoticeSports.this, "Notice added successfully", Toast.LENGTH_SHORT).show();
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
        else Toast.makeText(NoticeSports.this, "Please provide permissions...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 86 && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            String dataaaa = data.getData().getLastPathSegment();
            if (dataaaa.contains("/")) {
                String name = dataaaa.substring(dataaaa.lastIndexOf("/")+1);
                tvSportsFile.setText(name);
            }
            else tvSportsFile.setText(dataaaa);
        }
        else Toast.makeText(NoticeSports.this, "Please select a file...", Toast.LENGTH_SHORT).show();
    }
}
