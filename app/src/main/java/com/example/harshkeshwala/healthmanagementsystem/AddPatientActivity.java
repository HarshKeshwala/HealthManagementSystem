package com.example.harshkeshwala.healthmanagementsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class AddPatientActivity extends AppCompatActivity {


    //private Spinner departmentSpinner;

    private TextView mResult;
    private EditText FirstName,LastName,DOB,Address,Doctor;
    private Spinner Department;
    String Fname,Lname,Gdob,Gaddress,GDepartment,Gdoctor;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);



        Department=(Spinner)findViewById(R.id.departmentSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.departmentList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        Department.setAdapter(adapter);

        FirstName= (EditText)findViewById(R.id.editTextFirstName);
        LastName= (EditText)findViewById(R.id.editTextLastname);
        DOB= (EditText)findViewById(R.id.editTextDob);
        Address= (EditText)findViewById(R.id.editTextAddress);
        Doctor=(EditText)findViewById(R.id.editTextDoctor);
        button= (Button) findViewById(R.id.buttonAddPatient);
       // mResult=(TextView)findViewById(R.id.textViewresult);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fname = FirstName.getText().toString();
                Lname = LastName.getText().toString();
                Gdob = DOB.getText().toString();
                Gaddress=Address.getText().toString();
                GDepartment=Department.getSelectedItem().toString();
                Gdoctor=Doctor.getText().toString();
                new AddPatient().execute("https://nodem3.herokuapp.com/patients/");
                // new DeleteDataTask().execute("https://nodem3.herokuapp.com/patients/5c08069576474e0016fed5f1");
            }
        });

    }

    class AddPatient extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog =new ProgressDialog(AddPatientActivity.this);
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
          //  mResult.setText(result);
            if (progressDialog != null){
                progressDialog.dismiss();


                Toast toast = Toast.makeText(AddPatientActivity.this,
                        "Patient added successfully!"+result,
                        Toast.LENGTH_LONG);

                toast.show();

                Intent intent = new Intent(AddPatientActivity.this, ShowPatientsActivity.class);
                startActivity(intent);
            }
        }
        private String postData(String urlPath) throws IOException , JSONException {
            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;
            try {
                //create data to send to sever
                JSONObject dataToSend = new JSONObject();
                dataToSend.put("first_name",Fname);
                dataToSend.put("last_name",Lname);
                dataToSend.put("dob", Gdob);
                dataToSend.put("address",Gaddress);
                dataToSend.put("department", GDepartment);
                dataToSend.put("doctor", Gdoctor);
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