package com.example.harshkeshwala.healthmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

public class ShowPatientsActivity extends AppCompatActivity {

    private Button buttonAddPatient, buttonShowPatientDetails;
    private ListView patientsList;
    ProgressDialog pDialog;
    String data="";
    String TAG = "ShowPatients";

    ArrayList<HashMap<String, String>> patientArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_patients);


        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.patients, android.R.layout.simple_list_item_1);
        patientsList = (ListView)findViewById(R.id.showPatients);
       // patientsList.setAdapter(adapter);

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



    public class GetPatients extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ShowPatientsActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall("http://nodem3.herokuapp.com/patients/");

            if (jsonStr != null) {
                try {
//                    JSONObject jsonObj = new JSONObject(jsonStr);
//
//                    // Getting JSON Array node
//                    JSONArray contacts = jsonObj.getJSONArray("patients");

                    JSONArray array = new JSONArray(jsonStr);
                    for (int i = 0; i <array.length() ; i++) {

                        JSONObject jsonObject = array.optJSONObject(i);
                        String first_name = jsonObject.optString("first_name");
                        String last_name = jsonObject.optString("last_name");


                        HashMap<String, String> patient = new HashMap<>();

                        patient.put("first_name", first_name);
                        patient.put("last_name", last_name);

                        patientArrayList.add(patient);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    ShowPatientsActivity.this, patientArrayList,
                    R.layout.patient_list, new String[]{"first_name"},
                    new int[]{R.id.pName});

            patientsList.setAdapter(adapter);
        }

    }
}
