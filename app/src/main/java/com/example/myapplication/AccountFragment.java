package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.activity.result.ActivityResultLauncher;

import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;

import androidx.activity.result.ActivityResult;

import androidx.activity.result.ActivityResultCallback;

import androidx.activity.result.contract.ActivityResultContracts;


import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class AccountFragment extends Fragment {

    private TextView useremail, userID;
    private ImageView profilepicture;
    private Uri selectedImage;
    StorageReference storageReference;


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

        //sets the textviews to the user's current email and UID
        useremail.setText(user.getEmail());
        userID.setText(user.getUid());
//        getCurrentProfilePicStorageRef().getDownloadUrl().addOnCompleteListener(task -> {
//            if(task.isSuccessful())
//            {
//                Uri uri = task.getResult();
//                profilepicture.setImageURI(uri);
//            }
//        });

        //adds the click listeners
        addListeners();

        return view;
    }
    private void addListeners()
    {
        profilepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creates the intent to the photo selection screen
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //starts the activity with the above intent
                startActivityForResult(intent,3);

            }
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
            //The imageview is set to the selected image
            profilepicture.setImageURI(selectedImage);
            storageReference =  getCurrentProfilePicStorageRef();
            storageReference.putFile(selectedImage)
                    .addOnSuccessListener(task -> {
                Toast.makeText(getActivity(), "Profile Picture Updated!", Toast.LENGTH_LONG).show();
            }).addOnFailureListener(exception -> {
                        Toast.makeText(getActivity(), "Profile Picture Update Failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
                    });;
        }
    }
    //creates the storage Reference to the current firebase picture
    //the name of the reference is profile_pic, and the file inside is named the uid
    public StorageReference getCurrentProfilePicStorageRef() {return FirebaseStorage.getInstance().getReference().child("profile_pic").child(FirebaseAuth.getInstance().getUid());}
}