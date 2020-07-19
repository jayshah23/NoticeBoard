package com.example.noticeboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.viewholder> {

    ArrayList<notice> noticeList;
    Boolean fileStatus;
    public NoticeAdapter(ArrayList<notice> noticeList) {
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public NoticeAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_format, parent, false);
        return new NoticeAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.viewholder holder, final int position) {
        final String title, dept, sem, subject, notice, date, currdate, upload, time, type, key;
        title = noticeList.get(position).getTitle();
        dept = noticeList.get(position).getBranch();
        sem = noticeList.get(position).getSem();
        subject = noticeList.get(position).getSubject();
        notice = noticeList.get(position).getNotice();
        date = noticeList.get(position).getDate();
        currdate = noticeList.get(position).getCdate();
        upload = noticeList.get(position).getUpload();
        time = noticeList.get(position).getTime();
        type = noticeList.get(position).getType();
        key = noticeList.get(position).getKey();

        if (upload.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            holder.ibNoticeEdit.setVisibility(View.VISIBLE);
            holder.ibNoticeDelete.setVisibility(View.VISIBLE);
        }
        else {
            holder.ibNoticeEdit.setVisibility(View.GONE);
            holder.ibNoticeDelete.setVisibility(View.GONE);
        }

        holder.tvPrintTitle.setText(title);
        holder.tvPrintType.setText(type);
        holder.tvPrintUpload.setText(upload);
        holder.tvPrintDate.setText("Uploaded on : "+currdate);
        holder.tvPrintLDate.setText("Last date : "+date);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), NoticeDetails.class);
                i.putExtra("title", title);
                i.putExtra("dept", dept);
                i.putExtra("sem", sem);
                i.putExtra("subject", subject);
                i.putExtra("notice", notice);
                i.putExtra("date", date);
                i.putExtra("currdate", currdate);
                i.putExtra("upload", upload);
                i.putExtra("time", time);
                i.putExtra("key", key);
                v.getContext().startActivity(i);
            }
        });

        holder.ibNoticeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(), NoticeEdit.class).putExtra("key", key));
            }
        });

        FirebaseDatabase.getInstance().getReference("notice").child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("files")) {
                    fileStatus = true;
                    String file_name = dataSnapshot.child("files").getValue().toString();
                }
                else fileStatus = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        holder.ibNoticeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Delete Notice").setMessage("Do you really want to delete this Notice ?");
                builder.setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("notice").child(key).removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(v.getContext(), "Notice Delete Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Failed Database : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        if (fileStatus) {
                            FirebaseStorage.getInstance().getReference("uploads").child(key).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(v.getContext(), "Notice Files Deleted Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(v.getContext(), "Failed Storage : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
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
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        TextView tvPrintTitle, tvPrintType, tvPrintUpload, tvPrintDate, tvPrintLDate;
        ImageButton ibNoticeEdit, ibNoticeDelete;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tvPrintTitle = itemView.findViewById(R.id.tvPrintTitle);
            tvPrintType = itemView.findViewById(R.id.tvPrintType);
            tvPrintUpload = itemView.findViewById(R.id.tvPrintUpload);
            tvPrintDate = itemView.findViewById(R.id.tvPrintDate);
            tvPrintLDate = itemView.findViewById(R.id.tvPrintLDate);
            ibNoticeEdit = itemView.findViewById(R.id.ibNoticeEdit);
            ibNoticeDelete = itemView.findViewById(R.id.ibNoticeDelete);
        }
    }
}
