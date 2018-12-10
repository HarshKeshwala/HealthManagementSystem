package com.example.harshkeshwala.healthmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class PatientDetailsActivity extends AppCompatActivity {

    private Button buttonShowReport;
    private TextView pId;
    String patientId;
    private ProgressDialog pDialog;
    ListView lv;
    String data = "";

    public static TextView fName, lName, dob, address, department, doctor;
    String first_name;
    // URL to get contacts JSON
    private static String url = "https://nodem3.herokuapp.com/patients/";
    ArrayList<HashMap<String, String>> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        //Intent i = getIntent();
        //patientId = i.getStringExtra("pId");


        fName = (TextView)findViewById(R.id.first_name);
        lName = (TextView)findViewById(R.id.last_name);
        dob= (TextView)findViewById(R.id.dob);
        address = (TextView)findViewById(R.id.address);
        department = (TextView)findViewById(R.id.department);
        doctor = (TextView)findViewById(R.id.doctor);


        SharedPreferences prefs = getSharedPreferences("Patient", MODE_PRIVATE);
        patientId = prefs.getString("pId", null);

        buttonShowReport = (Button) findViewById(R.id.buttonShowReport);

        buttonShowReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PatientDetailsActivity.this, ShowReportActivity.class);
               // intent.putExtra("pId", patientId);
                startActivity(intent);
            }
        });
        pId = (TextView) findViewById(R.id.pid);

      //  pId.setText(patientId);

        new GetPatientDetails().execute();

    }

    private class GetPatientDetails extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PatientDetailsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL("https://nodem3.herokuapp.com/patients/"+patientId);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while(line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            try {
                JSONObject jsonObject = new JSONObject(data);
                fName.setText(jsonObject.getString("first_name"));
                lName.setText(jsonObject.getString("last_name"));
                dob.setText(jsonObject.getString("dob"));
                address.setText(jsonObject.getString("address"));
                department.setText(jsonObject.getString("department"));
                doctor.setText(jsonObject.getString("doctor"));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}