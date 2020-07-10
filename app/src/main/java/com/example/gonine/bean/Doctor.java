package com.example.gonine.bean;

import java.util.Vector;

public class Doctor {
    private int ID ;
    private String name ;
    private int age ;
    private Gender gender ;
    private String photo_path ;
    private Vector<Integer> patients ;
    private Vector<DoctorAdvice> advices ;

    public Doctor(int i, String n, int a, Gender g, String p, Vector<Integer> ps, Vector<DoctorAdvice> as ){
        this.ID = i ;
        this.name = n ;
        this.age = a ;
        this.gender = g ;
        this.photo_path = p ;
        this.patients = ps ;
        this.advices = as ;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPhoto_path() {
        return photo_path;
    }

    public Vector<Integer> getPatients() {
        return patients;
    }

    public Vector<DoctorAdvice> getAdvices() {
        return advices;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setPhoto_path(String photo_path) {
        this.photo_path = photo_path;
    }

    public void setPatients(Vector<Integer> patients) {
        this.patients = patients;
    }

    public void setAdvices(Vector<DoctorAdvice> advices) {
        this.advices = advices;
    }

    public void add_patients(int p){
        this.patients.add(p) ;
    }

    public void add_patients(int index, Integer m){
        this.patients.add(index,m) ;
    }

    public void remove_patients(int index){
        this.patients.remove(index) ;
    }

    public void clear_patients(){
        this.patients.clear() ;
    }

    public void add_advices(DoctorAdvice da){
        this.advices.add(da) ;
    }

    public void add_advices(int index, DoctorAdvice m){
        this.advices.add(index,m) ;
    }

    public void remove_advices(int index){
        this.advices.remove(index) ;
    }
    public void clear_advices(){
        this.advices.clear() ;
    }
}
