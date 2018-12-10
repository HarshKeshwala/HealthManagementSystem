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
    String dataParsed = "";
    String singleParsed = "";
    public static TextView mResult;

    // URL to get contacts JSON
    private static String url = "https://nodem3.herokuapp.com/patients/";
    ArrayList<HashMap<String, String>> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        mResult = (TextView) findViewById(R.id.text);
        Intent i = getIntent();
        //patientId = i.getStringExtra("pId");

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

        pId.setText(patientId);

        new GetPatientDetails().execute("https://nodem3.herokuapp.com/patients/"+patientId);

    }

    class GetPatientDetails extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            progressDialog = new ProgressDialog(PatientDetailsActivity.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.show();


        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return getData(params[0]);
            } catch (IOException ex) {
                return "Network error !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //set data responce to textview
            mResult.setText(result);

            //cancel progress dialog
            if (progressDialog != null) {
                progressDialog.dismiss();
                ;
            }

        }

        private String getData(String urlPath) throws IOException {

            {

                StringBuilder result = new StringBuilder();
                BufferedReader bufferedReader = null;

                try {
                    URL url = new URL(urlPath);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setReadTimeout(10000);//ms
                    urlConnection.setConnectTimeout(10000);//ms
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Content-Type", "application/json");//set header
                    urlConnection.connect();

                    //Read data from server
                    InputStream inputStream = urlConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line).append("\n");

                    }

                } finally {
                    {
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    }
                }
                return result.toString();
            }


        }

    }
}