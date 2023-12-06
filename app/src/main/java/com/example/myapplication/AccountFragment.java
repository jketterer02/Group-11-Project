package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

//import androidx.activity.result.ActivityResultLauncher;
//
//import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
//
//import androidx.activity.result.ActivityResult;
//
//import androidx.activity.result.ActivityResultCallback;
//
//import androidx.activity.result.contract.ActivityResultContracts;


import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;


public class AccountFragment extends Fragment {

    private TextView useremail, userID;
    private ImageView profilepicture, signout_button;
    private Uri selectedImage;


    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        //gets info of current User logged in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //puts the text views and imageview into variables for later use
        useremail = view.findViewById(R.id.Email);
        userID = view.findViewById(R.id.UID);
        profilepicture = view.findViewById(R.id.pfp);
        signout_button = view.findViewById(R.id.sign_out_button);

        //sets the textviews to the user's current email and UID
        useremail.setText(user.getEmail());
        userID.setText(user.getUid());

        //gets the current pfp storage reference, and downloads it from firebase
        //if this is successful, it puts the result into profilepicture
        getCurrentProfilePicStorageRef().getDownloadUrl().addOnCompleteListener(task -> {
            if(task.isSuccessful()) Glide.with(requireContext()).load(task.getResult()).into(profilepicture);
        });

        //adds the click listeners
        addListeners();

        return view;
    }
    private void addListeners()
    {
        profilepicture.setOnClickListener(v -> {
            //creates the intent to the photo selection screen
            Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            //starts the activity with the above intent
            startActivityForResult(intent,3);
        });

        signout_button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
    }
    //this code is depreciated but it's the only thing I can find that works
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK && data!=null){
            //puts the photo into a URI
            selectedImage = data.getData();
            //The the selectedImage is loaded into profile picture
            Glide.with(requireContext()).load(selectedImage).into(profilepicture);
            //puts the URI file into the firebase file named the user's UID in the "profile pic" folder
            getCurrentProfilePicStorageRef().putFile(selectedImage).addOnSuccessListener(task -> {
                Toast.makeText(getActivity(), "Profile Picture Updated!", Toast.LENGTH_LONG).show();
            });
        }
    }
    //creates the storage Reference to the current firebase picture
    //the name of the reference is profile_pic, and the file inside is named the uid of the current user
    public StorageReference getCurrentProfilePicStorageRef()
    {
        String uid = FirebaseAuth.getInstance().getUid();
        return FirebaseStorage.getInstance().getReference().child("profile_pic").child(uid);
    }
}