package com.example.harshkeshwala.healthmanagementsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShowReportActivity extends AppCompatActivity {


    private Button buttonAddReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_report);


        buttonAddReport = (Button)findViewById(R.id.buttonAddReport);
        buttonAddReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShowReportActivity.this, AddReportActivity.class);
                startActivity(intent);
            }
        });

    }
}
