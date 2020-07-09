package com.example.gonine.bean;

import com.google.firebase.Timestamp;

public class NoteItem implements Comparable {
    private Timestamp time;//用的firebase时间戳
    private String doctor_user_name ;
    private String content ;

    public NoteItem(){}
    public NoteItem(Timestamp _timestamp, String _doctor_user_name, String _content){
        this.time = _timestamp ;
        this.content = _content ;
        this.doctor_user_name = _doctor_user_name;
    }
    public Timestamp getTime() {
        return time;
    }

    public String getDoctorUserName() {
        return this.doctor_user_name;
    }

    public String getContent() {
        return content;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setDoctor_id(String _doctor_user_name) {
        this.doctor_user_name = _doctor_user_name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int compareTo(Object o) {
        NoteItem obj = (NoteItem) o;
        return time.compareTo(obj.time);
    }
}
