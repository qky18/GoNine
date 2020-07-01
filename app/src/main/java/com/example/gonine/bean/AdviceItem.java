package com.example.gonine.bean;

import android.util.Pair;

import java.util.Vector;

public class AdviceItem {
    private Vector<Pair<String,Float>> medicine;//统一用g做统计
    private Vector<String> notes ;

    public AdviceItem(Vector<Pair<String,Float>> m, Vector<String> n){
        this.medicine = m ;
        this.notes = n ;
    }
    public Vector<Pair<String,Float>> getMedicine() {
        return medicine;
    }

    public Vector<String> getNotes() {
        return notes;
    }

    public void setMedicine(Vector<Pair<String,Float>> medicine) {
        this.medicine = medicine;
    }

    public void setNotes(Vector<String> notes) {
        this.notes = notes;
    }

    public void add_medicine(Pair<String, Float> m){
        this.medicine.add(m) ;
    }

    public void add_medicine(int index, Pair<String, Float> m){
        this.medicine.add(index,m) ;
    }

    public void remove_medicine(int index){
        this.medicine.remove(index) ;
    }
    public void clear_medicine(){
        this.medicine.clear() ;
    }


    public void add_notes(String n){
        this.notes.add(n);
    }
    public void add_notes(int index, String m){
        this.notes.add(index,m) ;
    }

    public void remove_notes(int index){
        this.notes.remove(index) ;
    }
    public void clear_notes(){
        this.notes.clear() ;
    }

}