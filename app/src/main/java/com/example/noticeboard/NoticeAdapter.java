package com.example.noticeboard;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.viewholder> {

    ArrayList<notice> noticeList;
    public NoticeAdapter(ArrayList<notice> noticeList) {
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public NoticeAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_list, parent, false);
        return new NoticeAdapter.viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.viewholder holder, int position) {
        final String title, dept, sem, subject, notice, date, currdate, upload, time, type;
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

        final String j = String.valueOf(holder.getAdapterPosition());
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
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        TextView tvPrintTitle, tvPrintType, tvPrintUpload, tvPrintDate, tvPrintLDate;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            tvPrintTitle = itemView.findViewById(R.id.tvPrintTitle);
            tvPrintType = itemView.findViewById(R.id.tvPrintType);
            tvPrintUpload = itemView.findViewById(R.id.tvPrintUpload);
            tvPrintDate = itemView.findViewById(R.id.tvPrintDate);
            tvPrintLDate = itemView.findViewById(R.id.tvPrintLDate);
        }
    }
}
