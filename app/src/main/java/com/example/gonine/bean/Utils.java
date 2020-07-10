package com.example.gonine.bean;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

import java.util.concurrent.Executor;

public class Utils {

    public Utils(){}

    private Task<Void> addNoteItem(final DocumentReference patientRef, final NoteItem item, final String subCollectionName){
        final CollectionReference docRef = patientRef.collection(subCollectionName);
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        return fs.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                docRef.add(item);
                return null;
            }
        });
    }

    private Task<Void> addDigitalItem(final DocumentReference patientRef, final DigitalItem item, final String subCollectionName){
        final CollectionReference docRef = patientRef.collection(subCollectionName);
        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        return fs.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                docRef.add(item);
                return null;
            }
        });
    }

    public void addDoctorAdvice(DocumentReference patientRef, NoteItem item){
        Log.e("util","addDoctorAdvice");
        addNoteItem(patientRef, item, "doctor_advice");
        /*会报错ClassCastException，this不能转化为java.util.concurrent.Executor
        addNoteItem(patientRef, item, "doctor_advice").addOnSuccessListener((Executor) this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("doctor_advice", "doctor_advice added.");
            }
        }).addOnFailureListener((Executor) this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("doctor_advice", "add doctor_advice failed.");
            }
        });
        */
    }

    public void addDiagnosis(DocumentReference patientRef, NoteItem item){
        addNoteItem(patientRef, item, "diagnosis");
    }

    public void addDigital(DocumentReference patientRef, DigitalItem item, final String type){
        addDigitalItem(patientRef, item, type);
    }
}
