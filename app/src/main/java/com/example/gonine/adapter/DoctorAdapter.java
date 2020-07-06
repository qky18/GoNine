/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.gonine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.gonine.R;
import com.example.gonine.bean.Patient;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;


/**
 * RecyclerView adapter for a list of Patients.
 */
public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private List<Patient> patientList;

    public DoctorAdapter(List<Patient> patientList) {
        super();
        this.patientList = patientList;
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_patient, parent, false));
    }


    @Override
    public void onBindViewHolder(ViewHolder patientViewHolder, int i) {
        Patient patient = patientList.get(i);
        patientViewHolder.nameView.setText(patient.getName());
        // Load image via Glide lib
        Glide.with(patientViewHolder.imageView.getContext())
                .load(patient.getPhotoResID())
                .into(patientViewHolder.imageView);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameView;
        TextView categoryView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.patient_item_image);
            nameView = itemView.findViewById(R.id.patient_item_name);
        }

        public void bind(final DocumentSnapshot snapshot) {

            Patient patient = snapshot.toObject(Patient.class);

            // Load image via Glide lib
            Glide.with(imageView.getContext())
                    .load(patient.getPhotoResID())
                    .into(imageView);

            nameView.setText(patient.getName());
            //TODO: find a representation of severity
            //categoryView.setText(patient.getSeverity());

            /* TODO: parent selected listener
            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onPatientSelected(snapshot);
                    }
                }
            });
             */
        }

    }
}
