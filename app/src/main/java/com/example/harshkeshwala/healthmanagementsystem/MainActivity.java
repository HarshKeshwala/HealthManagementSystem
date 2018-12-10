package com.example.harshkeshwala.healthmanagementsystem;


import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText textInputUsername;
    private EditText textInputPassword;
    private Button buttonLogin;

    String inputUsername, inputPassword;

    private ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        imgView = (ImageView)findViewById(R.id.logo);
        imgView.setImageResource(R.drawable.logo_image);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textInputUsername = findViewById(R.id.editTextUsername);
                textInputPassword = findViewById(R.id.editTextPassword);



                if (!validateUsername() | !validatePassword()) {
                    return;
                }

                Intent intent = new Intent(MainActivity.this, ShowPatientsActivity.class);
                startActivity(intent);

            }
        });
    }

    private boolean validateUsername(){

        inputUsername = textInputUsername.getText().toString().trim();

        if(inputUsername.isEmpty()){
            textInputUsername.setError("Please enter username!");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }

    private boolean validatePassword(){

        inputPassword = textInputPassword.getText().toString().trim();

        if(inputPassword.isEmpty()){
            textInputPassword.setError("Please enter password!");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }

}
