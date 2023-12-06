package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private EditText newemail, newpass, newconfirmpass;
    private ImageButton createnewaccount, backtohome;
    private Intent loginintent;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        newemail = findViewById(R.id.createaccount_email);
        newpass = findViewById(R.id.createaccount_confirmpass);
        newconfirmpass = findViewById(R.id.createaccount_pass);
        createnewaccount = findViewById(R.id.accountcreate_button);
        backtohome = findViewById(R.id.back_button);
        loginintent = new Intent(this,LoginActivity.class);
        //Creates an instance of a firebaseauth that we can use to call the firebase API
        auth = FirebaseAuth.getInstance();
        addListeners();
    }
    private void addListeners()
    {
        createnewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = newemail.getText().toString();
                String pass = newpass.getText().toString();
                String confirmpass = newconfirmpass.getText().toString();
                //validation logic
                if(email.isEmpty()||pass.isEmpty()||confirmpass.isEmpty()) showMessage("Please Enter Login Credentials");
                else if(!pass.equals(confirmpass)) showMessage("Please Confirm Password");
                else registeruser(email,pass);
            }
        });
        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(loginintent);
                finish();
            }
        });

    }
    //This method creates a Toast Notification with the specified text
    private void showMessage(String text)
    {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
    //This method registers a user into firebase
    private void registeruser(String email, String pass)
    {
        //Creates the over 18 popup
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(true);
        builder.setTitle("New User Age Verification");
        builder.setMessage("Are you above the age of 18?");
        //if you click no, the dialog is dismissed and shows a notification
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showMessage("User Accounts must be over 18");
            }
        });
        //if you click yes, lets you register
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            showMessage("Account Creation Successful!");
                            startActivity(loginintent);
                            finish();
                        }
                        else showMessage(task.getException().getMessage());
                    }
                });
            }
        });
        builder.show();
    }

}