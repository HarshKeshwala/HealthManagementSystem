package com.example.harshkeshwala.healthmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShowPatientsActivity extends AppCompatActivity {

    private Button buttonAddPatient, buttonShowPatientDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_patients);


        buttonAddPatient = (Button) findViewById(R.id.buttonAddPatient);
        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShowPatientsActivity.this, AddPatientActivity.class);
                startActivity(intent);

            }
        });


        buttonShowPatientDetails = (Button) findViewById(R.id.buttonShowPatientDetails);
        buttonShowPatientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShowPatientsActivity.this, PatientDetailsActivity.class);
                startActivity(intent);
            }
        });
    }
}
