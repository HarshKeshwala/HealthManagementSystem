package com.example.harshkeshwala.healthmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddReportActivity extends AppCompatActivity {



    private EditText bloodPressure, heartRate, respiratoryRate, bloodOxygen;

    String blood_pressure, heart_rate, respiratory_rate, blood_oxygen;

    private Button buttonAddReport;

    String patientId;
    String inputblood_pressure, inputheart_rate, inputrespiratory_rate, inputblood_oxygen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);


//        Intent i = getIntent();
//        patientId = i.getStringExtra("patientId");

        SharedPreferences prefs = getSharedPreferences("Patient", MODE_PRIVATE);
        patientId = prefs.getString("pId", null);

        bloodPressure = (EditText)findViewById(R.id.editTextBloodpressure);
        heartRate = (EditText)findViewById(R.id.editTextHeartrate);
        respiratoryRate = (EditText)findViewById(R.id.editTextRespiratoryrate);
        bloodOxygen = (EditText)findViewById(R.id.editTextBloodoxygen);


        buttonAddReport = (Button) findViewById(R.id.buttonAddReport);

        buttonAddReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!ValidateBloodOxygen() | !ValidateBloodPressure() | !ValidateHeartRate() | !ValidateRespiratory() ) {
                   return;
                }

                blood_pressure =  bloodPressure.getText().toString();
                heart_rate = heartRate.getText().toString();
                respiratory_rate = respiratoryRate.getText().toString();
                blood_oxygen = bloodOxygen.getText().toString();

                new AddReport().execute("http://nodem3.herokuapp.com/patients/"+patientId+"/records");


            }
        });
    }


    private boolean ValidateBloodPressure(){

        inputblood_pressure = bloodPressure.getText().toString().trim();

        if(inputblood_pressure.isEmpty()){
            bloodPressure.setError("Please enter Blood Pressure!");
            return false;
        } else {
            bloodPressure.setError(null);
            return true;
        }
    }

    private boolean ValidateRespiratory(){

        inputrespiratory_rate = respiratoryRate.getText().toString().trim();

        if(inputrespiratory_rate.isEmpty()){
            respiratoryRate.setError("Please enter Respiratory Rate!");
            return false;
        } else {
            respiratoryRate.setError(null);
            return true;
        }
    }

    private boolean ValidateBloodOxygen(){

        inputblood_oxygen = bloodOxygen.getText().toString().trim();

        if(inputblood_oxygen.isEmpty()){
            bloodOxygen.setError("Please enter Blood Oxygen !");
            return false;
        } else {
            bloodOxygen.setError(null);
            return true;
        }
    }

    private boolean ValidateHeartRate(){

        inputheart_rate = heartRate.getText().toString().trim();

        if(inputheart_rate.isEmpty()){
            heartRate.setError("Please enter heart rate!");
            return false;
        } else {
            heartRate.setError(null);
            return true;
        }
    }

    class AddReport extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog =new ProgressDialog(AddReportActivity.this);
            progressDialog.setMessage("Inserting data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                return postData(params[0]);
            } catch (IOException ex) {
                return "network Error !";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog != null){
                progressDialog.dismiss();


                Toast toast = Toast.makeText(AddReportActivity.this,
                        "Record added successfully!",
                        Toast.LENGTH_LONG);

                toast.show();

                Intent i = new Intent(AddReportActivity.this, ShowReportActivity.class);
                startActivity(i);

            }
        }

        private String postData(String urlPath) throws IOException , JSONException {

            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;


            try {
                //create data to send to sever

                JSONObject dataToSend = new JSONObject();
                dataToSend.put("blood_pressure",blood_pressure);
                dataToSend.put("heart_rate",heart_rate);
                dataToSend.put("respiratory_rate", respiratory_rate);
                dataToSend.put("blood_oxygen",blood_oxygen);


                //Initialize and config request then connect server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);//ms
                urlConnection.setConnectTimeout(10000);//ms
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true); // enable output (body data)
                urlConnection.setRequestProperty("Content-Type", "application/json");//set header
                urlConnection.connect();

                // Write data into server.
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //read data response from server

                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {

                    result.append(line).append("\n");
                }
            }finally {
                if (bufferedReader != null){
                    bufferedReader.close();
                }
                if(bufferedWriter!=null){
                    bufferedWriter.close();
                }
            }

            return result.toString();
        }
    }
}
