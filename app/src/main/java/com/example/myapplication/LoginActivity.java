package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity
{
    private EditText signin_pass, signin_email;
    private ImageButton signin_button, registerLink, googlesignin;
    private TextView password_recovery;
    private Intent homeintent, registerintent, recoveryintent;
    private FirebaseAuth auth;

     //private GoogleSignInClient mGoogleSignInClient;
     //private final static int RC_SIGN_IN = 123; // Request code for sign-in

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //sets the content view to be the login activity, needed so the app doesn't instantly crash
        setContentView(R.layout.activity_login);
        //sets xml elements into variables
        signin_email = findViewById(R.id.login_email);
        signin_pass = findViewById(R.id.login_pass);
        signin_button = findViewById(R.id.login_button);
        password_recovery = findViewById(R.id.forgot_password);
        registerLink = findViewById(R.id.register_button);
        googlesignin = findViewById(R.id.google_button);
        //Creates intents to use to change screens
        homeintent = new Intent(this,MainActivity.class);
        registerintent = new Intent(this, RegisterActivity.class);
        recoveryintent = new Intent(this, RecoveryActivity.class);
        //Creates an instance of a firebaseauth that we can use to call the firebase API
        auth = FirebaseAuth.getInstance();
        // Configure Google Sign-In
        /*
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        */

        //Sets the listeners for clicks on the login
        addListeners();
    }
    private void addListeners() {
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Makes login button invisible when pressed
                signin_button.setVisibility(View.INVISIBLE);

                String emailstring = signin_email.getText().toString();
                String passwordstring = signin_pass.getText().toString();
                if (emailstring.isEmpty() || passwordstring.isEmpty()) {
                    //Makes login button visible again
                    signin_button.setVisibility(View.VISIBLE);
                    showMessage("Please Enter Login Credentials");
                } else {
                    login(emailstring, passwordstring);
                }
            }
        });

        //Listener for clicking register link
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(registerintent);
                finish();
            }
        });

        password_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(recoveryintent);
                finish();
            }
        });
        /*
        googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
        */
    }
/*
    }
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

 */
        /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                showMessage("Google sign in failed: " + e.getMessage());
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        showMessage("Google Sign In Success");
                        startActivity(homeintent);
                        finish();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("Google Sign In Failed: " + e.getMessage());
                        signin_button.setVisibility(View.VISIBLE);
                    }
                });
    }
*/
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
                    showMessage("Login Successful!");
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

