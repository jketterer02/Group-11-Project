package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsHolder> {

    private List<Article> articleList;

    //likeCounts are stored in a hashmap
    private Map<String, Integer> likecounts = new HashMap<>();

    //local database to store like counts
    //works between users
    private SharedPreferences sharedPreferences;

    NewsRecyclerAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        //creates the sharedPreferences database
        this.sharedPreferences = context.getSharedPreferences("likeCounts", Context.MODE_PRIVATE);
        initializeLikeCounts();
    }

    //Updates the like counts to what they should be when loaded into the newsview
    private void initializeLikeCounts() {
        likecounts.clear();
        for (int i=0;i<articleList.size();i++)
        {
            Article article = articleList.get(i);
            // Retrieve the like count from SharedPreferences, defaults to 0 if not found
            int storedLikeCount = sharedPreferences.getInt(article.getUrl(), 0);
            // Puts the value from Shared Preferences into the likeCounts HashMap
            likecounts.put(article.getUrl(), storedLikeCount);
        }
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.sourceTextView.setText(article.getSource().getName());
        Picasso.get().load(article.getUrlToImage()).error(R.drawable.error_image_icon).placeholder(R.drawable.error_image_icon).into(holder.imageView);

        holder.itemView.setOnClickListener((view -> {
            Intent intent = new Intent(view.getContext(), ViewNewsActivity.class);
            intent.putExtra("url", article.getUrl());
            view.getContext().startActivity(intent);
        }));

        // Sets the like count for the current article from the HashMap
        holder.like_count.setText(String.valueOf(likecounts.get(article.getUrl())));

        holder.likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current like count for the article from the HashMap
                int currentLikeCount = likecounts.get(article.getUrl());
                //if the holder was liked, unlike the button and decrease the like count, else like the button and increase the like count
                if (holder.liked)
                {
                    holder.likebutton.setImageResource(R.drawable.like_button);
                    likecounts.put(article.getUrl(), currentLikeCount - 1);
                }
                else
                {
                    holder.likebutton.setImageResource(R.drawable.liked_button);
                    likecounts.put(article.getUrl(), currentLikeCount + 1);
                }
                //Update the like count TextView
                holder.like_count.setText(String.valueOf(likecounts.get(article.getUrl())));
                //flip the value of isliked
                holder.liked = !holder.liked;
                //update the database of likes
                sharedPreferences.edit().putInt(article.getUrl(), likecounts.get(article.getUrl())).apply();
            }
        });
    }

    //updates the list of articles
    void updateRecyclerView(List<Article> data)
    {
        articleList.clear();
        articleList.addAll(data);
        initializeLikeCounts();
    }

    @Override
    public int getItemCount()
    {
        return articleList.size();
    }

    static class NewsHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView sourceTextView;
        ImageView imageView;
        ImageButton likebutton;
        TextView like_count;
        boolean liked = false;

        NewsHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_title);
            sourceTextView = itemView.findViewById(R.id.article_source);
            imageView = itemView.findViewById(R.id.article_image);
            likebutton = itemView.findViewById(R.id.like_button);
            like_count = itemView.findViewById(R.id.like_count);

            likebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (liked) likebutton.setImageResource(R.drawable.like_button);
                    else likebutton.setImageResource(R.drawable.liked_button);
                    liked = !liked;
                }
            });
        }
    }
}
