package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.myapplication.databinding.ActivityLoginBinding;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private EditText signin_pass, signin_email;
    private Button signin_button;
    private TextView registerLink;
    private Intent homeintent;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //sets the content view to be the login activity, needed so the app doesn't instantly crash
        setContentView(R.layout.activity_login);
        signin_email = findViewById(R.id.login_email);
        signin_pass = findViewById(R.id.login_pass);
        signin_button = findViewById(R.id.login_button);
        registerLink = findViewById(R.id.register_button);
        homeintent = new Intent(this, MainActivity.class);
        //registerintent = new Intent(this, RegisterActivity.class);
        //Creates an instance of a firebaseauth that we can use to call the firebase API
        auth = FirebaseAuth.getInstance();

        //Sets the listeners for clicks on the login
        addListeners();
    }
    private void addListeners()
    {
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Makes login button invisible when pressed
                signin_button.setVisibility(View.INVISIBLE);

                String emailstring = signin_email.getText().toString();
                String passwordstring = signin_pass.getText().toString();
                if(emailstring.isEmpty()||passwordstring.isEmpty())
                {
                    //Makes login button visible again
                    signin_button.setVisibility(View.VISIBLE);
                    showMessage("Invalid Login Credentials");
                }
                else
                {
                    login(emailstring,passwordstring);
                }
            }
        });

        //Listener for clicking register link
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setContentView(R.layout.activity_register);
                //startActivity(registerintent);
                //finish();
            }
        });
    }

    //This method creates a Toast Notification with the specified text
    private void showMessage(String text)
    {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
    //This method authorizes the login credentials
    private void login(String x, String y)
    {
        auth.signInWithEmailAndPassword(x,y).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
            //this method is called when we get back a response from the Firebase server
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    //redirects to main activity
                    //setContentView(R.layout.activity_main);
                    startActivity(homeintent);
                    finish();
                }
                else
                {
                    signin_button.setVisibility(View.VISIBLE);
                    //Creates a toast with the exception method upon failure
                    showMessage(task.getException().getMessage());
                }
            }
        });
    }

}