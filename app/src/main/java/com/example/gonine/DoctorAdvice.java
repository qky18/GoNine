package com.example.gonine;

import com.google.firebase.Timestamp;

public class DoctorAdvice {
    private Timestamp time;//用的firebase时间戳
    private int doctor_id ;
    private int patient_id ;
    private AdviceItem content ;

    public DoctorAdvice(Timestamp t, int di, int pi, AdviceItem c){
        this.time = t ;
        this.doctor_id = di ;
        this.patient_id = pi ;
        this.content = c ;
    }
    public Timestamp getTime() {
        return time;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public int getPatient_id() {
        return patient_id;
    }

    public AdviceItem getContent() {
        return content;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public void setPatient_id(int patient_id) {
        this.patient_id = patient_id;
    }

    public void setContent(AdviceItem content) {
        this.content = content;
    }
}
