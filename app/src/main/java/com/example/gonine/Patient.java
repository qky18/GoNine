package com.example.gonine;


import java.util.Vector;

public class Patient {
    private int ID ;
    private String name ;
    private Gender gender ;
    private Severity severity ;
    private String photo_path  ;
    private Vector<DoctorAdvice> advices;
    private Vector<MedicalDataItem> data ;
    private int age ;

    public Patient(int i,String n,Gender g, Severity s, String p, Vector<DoctorAdvice> da, Vector<MedicalDataItem> md,int a){
        this.ID = i ;
        this.name = n ;
        this.gender = g ;
        this.severity = s ;
        this.photo_path = p ;
        this.advices = da ;
        this.data = md ;
        this.age = a ;

    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public Vector<DoctorAdvice> getDoctor_advices() {
        return advices;
    }

    public Vector<MedicalDataItem> getMedical_data() {
        return data;
    }

    public int getAge() {
        return age;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public void setAdvices(Vector<DoctorAdvice> advices) {
        this.advices = advices;
    }

    public void setData(Vector<MedicalDataItem> data) {
        this.data = data;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public void add_advices(DoctorAdvice da){
        this.advices.add(da) ;
    }

    public void add_advices(int index,DoctorAdvice da){
        this.advices.add(index,da) ;
    }

    public void remove_advices(int index){
        this.advices.remove(index) ;
    }

    public void clear_advices(){
        this.advices.clear() ;
    }

    public void add_data(MedicalDataItem m){
        this.data.add(m) ;
    }

    public void add_data(int index, MedicalDataItem m){
        this.data.add(index,m) ;
    }

    public void remove_data(int index){
        this.data.remove(index) ;
    }
    public void clear_data(){
        this.data.clear() ;
    }
}




