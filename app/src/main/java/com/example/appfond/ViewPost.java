package com.example.appfond;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ImageSpan;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ViewPost extends AppCompatActivity {

   // Bundle bundle = getIntent().getExtras();
   //Intent intent = getIntent();
   // String value = getIntent().getStringExtra("textPost");;


    private TextView field_post_text;
    private TextView field_date_post;
    private TextView field_post_desc;
    private Uri postImageUri;
    private ImageView setupImage;
    private String idPost;
    private Toolbar postToolbar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        postToolbar = findViewById(R.id.viewPostToolbar);
        setSupportActionBar(postToolbar);
        getSupportActionBar().setTitle("Назад");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        field_post_text = findViewById(R.id.fieldtextPost);
        setupImage = findViewById(R.id.imagePostObj);
        field_date_post = findViewById(R.id.fieldDatePost);
        field_post_desc = findViewById(R.id.fieldDescText);

        Intent intent = this.getIntent();



        //get value from clild
        RequestOptions placeholderRequest = new RequestOptions();
        placeholderRequest.placeholder(R.drawable.default_profile);

        String fText = intent.getStringExtra("textPost");
        String imagePost = intent.getStringExtra("imagePost");
        String postDateValue = intent.getStringExtra("postDate");
        String postDescValue = intent.getStringExtra("postDesc");
        idPost = intent.getStringExtra("postId");

        //set value

        field_post_text.setText(fText);
        field_date_post.setText(postDateValue);
        field_post_desc.setText(postDescValue);

        Glide.with(this).setDefaultRequestOptions(placeholderRequest).load(imagePost).into(setupImage);
        postImageUri = Uri.parse(imagePost);
        Toast.makeText(ViewPost.this, idPost, Toast.LENGTH_SHORT).show();

        field_post_text.setMovementMethod(new ScrollingMovementMethod());

    }
}