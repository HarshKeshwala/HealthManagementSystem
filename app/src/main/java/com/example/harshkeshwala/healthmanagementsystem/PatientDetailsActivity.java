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
        patientId = i.getStringExtra("pId");

        buttonShowReport = (Button) findViewById(R.id.buttonShowReport);

        buttonShowReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PatientDetailsActivity.this, ShowReportActivity.class);
                //  intent.putExtra("pId",.getText().toString());
                startActivity(intent);
            }
        });
        pId = (TextView) findViewById(R.id.pid);

        pId.setText(patientId);

        new GetDataTask().execute("https://nodem3.herokuapp.com/patients/"+patientId);
//        fetchData process = new fetchData();
//        process.execute();

    }

    //    public class fetchData extends AsyncTask<Void, Void, Void> {
//
//        String data = "";
//        String dataParsed = "";
//        String singleParsed = "";
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//
//            try {
//                URL url = new URL("https://nodem3.herokuapp.com/patients/"+patientId);
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String line = "";
//                while (line != null) {
//                    line = bufferedReader.readLine();
//                    data = data + line;
//
//                }
//
//                JSONArray JA = new JSONArray(data);
//
//                for (int i = 0; i < JA.length(); i++) {
//                    JSONObject JO = (JSONObject) JA.get(i);
//
//                    singleParsed = "First Name: " + JO.get("first_name") + ";" + "\n" +
//                            "Last Name: " + JO.get("last_name") + ";" + "\n" +
//                            "Date of birth: " + JO.get("dob") + ";" + "\n" +
//                            "address: " + JO.get("address") + ";" + "\n" +
//                            "department " + JO.get("department") + ";" + "\n" +
//                            "doctor: " + JO.get("doctor") + ";" + "\n" +
//                            "ID" + JO.get("_id");
//                    dataParsed = dataParsed + singleParsed + "\n";
//
//
//                    String id = JO.getString("_id");
//                    String first_name = JO.getString("first_name");
//                    String last_name = JO.getString("last_name");
//                    String dob = JO.getString("dob");
//                    String address = JO.getString("address");
//                    String department = JO.getString("department");
//                    String doctor = JO.getString("doctor");
//
//
//                }
//
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            mTextViewResult.setText(this.dataParsed);
//        }
//    }
    class GetDataTask extends AsyncTask<String, Void, String> {

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