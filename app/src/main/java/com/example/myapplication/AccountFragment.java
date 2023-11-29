package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AccountFragment extends Fragment {

    private TextView useremail, userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        //gets info of current User loggged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //creates variables to edit the email and UID textviews
        useremail = view.findViewById(R.id.Email);
        userID = view.findViewById(R.id.UID);
        //sets the textviews to the user's current email and UID
        useremail.setText(user.getEmail());
        userID.setText(user.getUid());

        return view;
    }
}