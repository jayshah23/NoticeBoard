package com.example.noticeboard;

public class notice {
    String title,branch,sem,subject,notice,date,cdate,upload,time,type,key;

    public notice(String title, String branch, String sem, String subject, String notice,
                  String date, String cdate, String upload, String time, String type, String key) {
        this.title = title;
        this.branch = branch;
        this.sem = sem;
        this.subject = subject;
        this.notice = notice;
        this.date = date;
        this.cdate = cdate;
        this.upload = upload;
        this.time = time;
        this.type = type;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public notice(){} // an empty public constructor required

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "title :"+title+"\nbranch :"+branch+"\nsem :"+sem+"\nsubject :"+subject+"\nnotice :"+notice+"\ndate :"+
                date+"\nCurrent date :"+cdate+"\nUploaded by : "+upload+"\ntime :"+time+"\ntype : "+type+"\n\n";
    }
}
