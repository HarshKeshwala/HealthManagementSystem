package com.example.harshkeshwala.healthmanagementsystem;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textInputUsername = findViewById(R.id.editTextUsername);
                textInputPassword = findViewById(R.id.editTextPassword);


                if (!validateUsername() | !validatePassword()) {
                    return;
                }

                Intent i = new Intent(MainActivity.this, ViewPatient.class);
                startActivity(i);
            }
        });
    }

    private boolean validateUsername(){

        String inputUsername = textInputUsername.getEditText().getText().toString().trim();

        if(inputUsername.isEmpty()){
            textInputUsername.setError("Please enter username!");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){

        String inputUsername = textInputPassword.getEditText().getText().toString().trim();

        if(inputUsername.isEmpty()){
            textInputPassword.setError("Please enter password!");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }
}
