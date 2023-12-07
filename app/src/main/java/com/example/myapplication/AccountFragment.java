package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;
//import com.google.firebase.storage.UploadTask;


public class AccountFragment extends Fragment {

    private TextView useremail, userID;
    private ImageView profilepicture, signout_button, likes, goback;
    private Uri selectedImage;

    private CheckBox genenral_checkbox, business_checkbox, sports_checkbox, tech_checkbox, health_checkbox, entertainment_checkbox, science_checkbox;

    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;

    String categories = "";



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
        likes = view.findViewById(R.id.likelist);
        goback = view.findViewById(R.id.exit_button);

        //finding Checkbox through their unique IDs
        genenral_checkbox = view.findViewById(R.id.checkbox_general);
        business_checkbox = view.findViewById(R.id.checkbox_business);
        sports_checkbox = view.findViewById(R.id.checkbox_sports);
        tech_checkbox = view.findViewById(R.id.checkbox_tech);
        health_checkbox = view.findViewById(R.id.checkbox_health);
        entertainment_checkbox = view.findViewById(R.id.bcheckbox_entertainment);
        science_checkbox = view.findViewById(R.id.checkbox_science);


        //sets the textviews to the user's current email and UID
        useremail.setText(user.getEmail());
        userID.setText(user.getUid());

        recyclerView = (RecyclerView) view.findViewById(R.id.news_recyclerView);
        progressIndicator = (LinearProgressIndicator) view.findViewById(R.id.progress_line);

        //gets the current pfp storage reference, and downloads it from firebase
        //if this is successful, it puts the result into profilepicture
        getCurrentProfilePicStorageRef().getDownloadUrl().addOnCompleteListener(task -> {
            if(task.isSuccessful()) Glide.with(requireContext()).load(task.getResult()).into(profilepicture);
        });

        progressIndicator.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        goback.setVisibility(View.INVISIBLE);
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

        likes.setOnClickListener(v -> {
            setupRecyclerView();
            getNews("GENERAL", null);
            recyclerView.setVisibility(View.VISIBLE);
            goback.setVisibility(View.VISIBLE);
        });
        goback.setOnClickListener(v -> {
            recyclerView.setVisibility(View.INVISIBLE);
            goback.setVisibility(View.INVISIBLE);
        });

        genenral_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(genenral_checkbox.isChecked()==true) {
                    categories += "general";
                    Log.d("CHECKBOX", categories);
                } else {
                    categories = categories.replace("general", "");
                    Log.d("CHECKBOX UNCHECKED", categories);
                }
            }
        });

        business_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(business_checkbox.isChecked()==true) {
                    categories += "business";
                    Log.d("CHECKBOX", categories);
                } else {
                    categories = categories.replace("business", "");
                    Log.d("CHECKBOX UNCHECKED", categories);
                }
            }
        });

        sports_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(sports_checkbox.isChecked()==true) {
                    categories += "sports";
                    Log.d("CHECKBOX", categories);
                } else {
                    categories = categories.replace("sports", "");
                    Log.d("CHECKBOX UNCHECKED", categories);
                }
            }
        });

        tech_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                categories += "technology";
                if(tech_checkbox.isChecked()==true) {
                    Log.d("CHECKBOX", categories);
                } else {
                    categories = categories.replace("technology", "");
                    Log.d("CHECKBOX UNCHECKED", categories);
                }
            }
        });

        health_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(health_checkbox.isChecked()==true) {
                    categories += "health";
                    Log.d("CHECKBOX", categories);
                } else {
                    categories = categories.replace("health", "");
                    Log.d("CHECKBOX UNCHECKED", categories);
                }
            }
        });

        entertainment_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(entertainment_checkbox.isChecked()==true) {
                    categories += "entertainment";
                    Log.d("CHECKBOX", categories);
                } else {
                    categories = categories.replace("entertainment", "");
                    Log.d("CHECKBOX UNCHECKED", categories);
                }
            }
        });

        science_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(science_checkbox.isChecked()==true) {
                    categories += "science";
                    Log.d("CHECKBOX", categories);
                } else {
                    categories = categories.replace("science", "");
                    Log.d("CHECKBOX UNCHECKED", categories);
                }
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

    void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsRecyclerAdapter(articleList, requireContext());
        recyclerView.setAdapter(adapter);
    }

    void changeInProgress(boolean showChange){
        if(showChange)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
    }

    void getNews(String category, String query){
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("d70ae652c05b44ff97b52bd864da464c");
        newsApiClient.getEverything(
                new EverythingRequest.Builder().language("en").q("general").from("2023-11-29").sortBy("popularity").build(),

                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {

                        getActivity().runOnUiThread(()->{
                            changeInProgress(false);
                            articleList = response.getArticles();
                            adapter.updateRecyclerView(articleList);

                            adapter.notifyDataSetChanged();
                        });

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("Failure!", throwable.getMessage());
                    }
                }

        );

    }
}