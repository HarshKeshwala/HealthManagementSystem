package com.example.harshkeshwala.healthmanagementsystem;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
public class ShowPatientsActivity extends AppCompatActivity {
    private ProgressDialog pDialog;
    ListView lv;
    String data = "";

    ArrayList<HashMap<String, String>> patientList;

    private Button buttonAddPatient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_patients);
        patientList = new ArrayList<>();
        lv = (ListView) findViewById(R.id.showPatients);
        new GetPatient().execute();


        buttonAddPatient = (Button) findViewById(R.id.buttonAddPatient);
        buttonAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ShowPatientsActivity.this, AddPatientActivity.class);
                startActivity(intent);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView id = (TextView) view.findViewById(R.id.twId);
                TextView fName = (TextView) view.findViewById(R.id.twFirstName);
                Intent intent = new Intent(ShowPatientsActivity.this,PatientDetailsActivity.class);
                //intent.putExtra("pId",id.getText().toString());

                SharedPreferences.Editor editor = getSharedPreferences("Patient", MODE_PRIVATE).edit();
                editor.putString("pId", id.getText().toString());
                editor.putString("fName", fName.getText().toString());
                editor.apply();
                startActivity(intent);
            }
        });
    }
    private class GetPatient extends AsyncTask<Void, Void, Void> {
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
            try {
                URL url = new URL("https://nodem3.herokuapp.com/patients");
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
                    String first_name = JO.getString("first_name");
                    String last_name = JO.getString("last_name");
                    String dob = JO.getString("dob");
                    String address = JO.getString("address");
                    String department = JO.getString("department");
                    String doctor = JO.getString("doctor");
                    HashMap<String, String> patient = new HashMap<>();
                    patient.put("_id", id);
                    patient.put("first_name", first_name);
                    patient.put("last_name", last_name);
                    patient.put("department", department);
                    patientList.add(patient);
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
                    ShowPatientsActivity.this, patientList,
                    R.layout.list_item, new String[]{"_id","first_name", "last_name",
                    "department"}, new int[]{R.id.twId,R.id.twFirstName,
                    R.id.twLastName, R.id.twDepartment});
            lv.setAdapter(adapter);
        }
    }
}