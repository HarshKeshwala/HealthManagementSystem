package com.example.harshkeshwala.healthmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
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
    ListView listPatientDetail;

    String data = "";

    ArrayList<HashMap<String, String>> patientArrayList;

    ListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        Intent i = getIntent();
        patientId = i.getStringExtra("pId");

        patientArrayList = new ArrayList<>();

        listPatientDetail = (ListView)findViewById(R.id.patientDetails);

        new GetPatientDetails().execute();


        buttonShowReport = (Button) findViewById(R.id.buttonShowReport);

        buttonShowReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PatientDetailsActivity.this, ShowReportActivity.class);
                intent.putExtra("pId", patientId);
                startActivity(intent);
            }
        });

        pId = (TextView) findViewById(R.id.pid);

        //pId.setText(patientId);

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

                pId.setText(url.toString());
                while(line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }
                JSONArray JA = new JSONArray(data);
                for (int i = 0; i <JA.length(); i++) {
                    JSONObject JO = (JSONObject) JA.get(i);
                    String first_name = JO.getString("first_name");
                    String last_name = JO.getString("last_name");
                    String dob = JO.getString("dob");
                    String address = JO.getString("address");
                    String department = JO.getString("department");
                    String doctor = JO.getString("doctor");

                    HashMap<String, String> patient = new HashMap<>();
                    patient.put("first_name", first_name);
                    patient.put("last_name", last_name);
                    patient.put("department", department);

                    patientArrayList.add(patient);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    PatientDetailsActivity.this, patientArrayList,
                    R.layout.patient_details, new String[]{"first_name", "last_name"}, new int[]{R.id.first_name,
                    R.id.last_name});
            listPatientDetail.setAdapter(adapter);
        }
    }

}
