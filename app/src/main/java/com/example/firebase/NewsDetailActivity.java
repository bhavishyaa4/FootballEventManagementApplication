package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        ImageView newsImageView = findViewById(R.id.newsImageView);
        TextView newsTitle = findViewById(R.id.newsTitle);
        TextView newsDescription = findViewById(R.id.newsDescription);

        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("image_url");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        // Load image using Glide
        Glide.with(this).load(imageUrl).into(newsImageView);

        newsTitle.setText(title);
        newsDescription.setText(description);  // Show full description
    }
}
