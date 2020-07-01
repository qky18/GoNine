package com.example.gonine.bean;

import android.util.Pair;

import com.google.firebase.Timestamp;

import java.util.Vector;

public class MedicalDataItem {
    private String name ;
    private Vector<Pair<Timestamp,Float>> data ;

    public MedicalDataItem(String n, Vector<Pair<Timestamp,Float>> d){
        this.name = n ;
        this.data = d ;
    }
    public String getName() {
        return name;
    }

    public  Vector<Pair<Timestamp,Float>> getData() {
        return data;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setData( Vector<Pair<Timestamp,Float>> data) {
        this.data = data;
    }

    public void add_data(Pair<Timestamp,Float> n){
        this.data.add(n);
    }
    public void add_data(int index, Pair<Timestamp,Float> m){
        this.data.add(index,m) ;
    }

    public void remove_data(int index){
        this.data.remove(index) ;
    }
    public void clear_notes(){
        this.data.clear() ;
    }
}
