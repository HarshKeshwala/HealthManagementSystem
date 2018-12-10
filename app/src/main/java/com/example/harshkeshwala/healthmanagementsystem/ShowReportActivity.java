package com.example.harshkeshwala.healthmanagementsystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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

public class ShowReportActivity extends AppCompatActivity {


    private Button buttonAddReport, buttonDeleteReport;

    private ProgressDialog pDialog;

    ListView listReport;
    String data = "";

    ArrayList<HashMap<String, String>> reportArrayList;
    String patientID;

    private TextView label;
    String selectedItem;
    String record_id;

    private final Context context = this;

    ListAdapter adapter;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_report);

        final Intent i = getIntent();

        patientID = i.getStringExtra("pId");

        label = (TextView) findViewById(R.id.reportLabel);
        label.setText(patientID);

        reportArrayList = new ArrayList<>();
        listReport = (ListView) findViewById(R.id.showReports);

        new GetReport().execute();

        buttonAddReport = (Button)findViewById(R.id.buttonAddReport);
        buttonAddReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShowReportActivity.this, AddReportActivity.class);
                intent.putExtra("patientId", patientID);
                startActivity(intent);
            }
        });


        OnItemLongClickListener itemLongListener = new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long rowid) {

                // Store selected item in global variable
                selectedItem = parent.getItemAtPosition(position).toString();

                TextView id = (TextView) v.findViewById(R.id.reportId);

                record_id = id.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove this record?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                       new DeleteReport().execute("https://nodem3.herokuapp.com/patients/"+patientID+"/records/"+record_id);


                       Intent i = new Intent(ShowReportActivity.this, PatientDetailsActivity.class);
                       startActivity(i);
//                        Toast.makeText(
//                                getApplicationContext(),
//                                record_id + " has been removed.",
//                                Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Create and show the dialog
                builder.show();

                // Signal OK to avoid further processing of the long click
                return true;
            }
        };

        listReport.setOnItemLongClickListener(itemLongListener);

    }


    private class GetReport extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(ShowReportActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL("https://nodem3.herokuapp.com/patients/"+patientID+"/records");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while(line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }
                JSONArray JA = new JSONArray(data);
                for (int i = 0; i <JA.length(); i++) {
                    JSONObject JO = (JSONObject) JA.get(i);
                    String id = JO.getString("_id");
                    String bloodPressure = JO.getString("blood_pressure");
                    String heartRate = JO.getString("heart_rate");
                    String respiratoryRate = JO.getString("respiratory_rate");
                    String bloodOxygen = JO.getString("blood_oxygen");

                    HashMap<String, String> report = new HashMap<>();
                    report.put("_id", id);
                    report.put("blood_pressure", bloodPressure);
                    report.put("heart_rate", heartRate);
                    report.put("respiratory_rate", respiratoryRate);
                    report.put("blood_oxygen", bloodOxygen);

                    reportArrayList.add(report);
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
            adapter = new SimpleAdapter(
                    ShowReportActivity.this, reportArrayList,
                    R.layout.report_list, new String[]{"_id","blood_pressure", "heart_rate",
                    "respiratory_rate", "blood_oxygen"}, new int[]{R.id.reportId,R.id.blood_pressure,
                    R.id.heart_rate, R.id.respiratory_rate, R.id.blood_oxygen});
            listReport.setAdapter(adapter);
        }
    }

    class DeleteReport extends  AsyncTask<String,Void,String>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ShowReportActivity.this);
            progressDialog.setMessage("Deleting data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                return deleteData(params[0]);}
            catch (IOException ex){
                return "Network error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(progressDialog !=null){
                progressDialog.dismiss();
            }

            Toast.makeText(
                    getApplicationContext(),
                     "Record has been removed.",
                    Toast.LENGTH_SHORT).show();

        }

        private String deleteData(String urlPath) throws IOException{

            String result = null;

            //initialize and config request, than connect to server
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);//ms
            urlConnection.setConnectTimeout(10000);//ms
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setRequestProperty("Content-Type", "application/json");//set header
            urlConnection.connect();

            //check delete successful or not
            if(urlConnection.getResponseCode() == 204){
                result = "Delete Successfully !";
            }else {
                result = "Delete failed !";
            }

            return result;
        }

    }
}
