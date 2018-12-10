//package com.example.harshkeshwala.healthmanagementsystem;
//
//
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.SimpleAdapter;
//import android.widget.Toast;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class ShowPatient extends AppCompatActivity {
//
//    private String TAG = ShowPatient.class.getSimpleName();
//
//    private ProgressDialog pDialog;
//    ListView lv;
//    String data = "";
//    String dataParsed = "";
//    String singleParsed = "";
//
//
//    // URL to get contacts JSON
//    private static String url = "https://nodem3.herokuapp.com/patients/";
//
//    ArrayList<HashMap<String, String>> patientList;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_show_patient);
//
//        patientList = new ArrayList<>();
//
//        ListView lv = (ListView) findViewById(R.id.list);
//        new GetPatient().execute();
//    }
//
//    private class GetPatient extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Showing progress dialog
//            pDialog = new ProgressDialog(ShowPatient.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... arg0) {
//
//            try {
//                URL url = new URL("https://nodem3.herokuapp.com/patients");
//
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
//                InputStream inputStream = httpURLConnection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//                String line = "";
//                while(line != null) {
//                    line = bufferedReader.readLine();
//                    data = data + line;
//
//                }
//
//                JSONArray JA = new JSONArray(data);
//
//                for (int i = 0; i <JA.length(); i++) {
//                    JSONObject JO = (JSONObject) JA.get(i);
//                    String id = JO.getString("_id");
//                    String first_name = JO.getString("first_name");
//                    String last_name = JO.getString("last_name");
//                    String dob = JO.getString("dob");
//                    String address = JO.getString("address");
//                    String department = JO.getString("department");
//                    String doctor = JO.getString("doctor");
//
//                    HashMap<String, String> patient = new HashMap<>();
//
//                    patient.put("_id", id);
//                    patient.put("first_name", first_name);
//                    patient.put("last_name", last_name);
//                    patient.put("department", department);
//
//                    patientList.add(patient);
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
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            // Dismiss the progress dialog
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//            /**
//             * Updating parsed JSON data into ListView
//             * */
//            ListAdapter adapter = new SimpleAdapter(
//                    ShowPatient.this, patientList,
//                    R.layout.list_item, new String[]{"first_name", "last_name",
//                    "department"}, new int[]{R.id.twFirstName,
//                    R.id.twLastName, R.id.twDepartment});
//            lv.setAdapter(adapter);
//        }
//
//    }
//}
//
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//
//class act extends AppCompatActivity implements View.OnClickListener {
//
//    findViewById(R.id.button).setOnClickListener(this);
//    @Override
//    public void onClick(View v) {
//        switch (v.getId())  {
//            case R.id.button:
//                break;
//        }
//    }
//}