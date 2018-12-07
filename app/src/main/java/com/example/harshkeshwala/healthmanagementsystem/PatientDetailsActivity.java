package com.example.harshkeshwala.healthmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PatientDetailsActivity extends AppCompatActivity {

    private Button buttonShowReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);


        buttonShowReport = (Button) findViewById(R.id.buttonShowReport);

        buttonShowReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PatientDetailsActivity.this, ShowReportActivity.class);
                startActivity(intent);
            }
        });
    }
}
