package com.example.appfond;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class NoCardActivity extends AppCompatActivity {

    private Button createCard;
    private Button cancelCreateCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_no_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView imageView = findViewById(R.id.imageLogoNoCard);

        // Установка изображения с закругленными углами с помощью Glide
        RequestOptions requestOptions = new RequestOptions()
                .transform(new RoundedCorners(50)); // Радиус закругления углов в пикселях

        Glide.with(this)
                .load(R.drawable.epicheck_logo) // Замените на ваш ресурс изображения
                .apply(requestOptions)
                .into(imageView);


        createCard = findViewById(R.id.buttonCreateCard);
        cancelCreateCard = findViewById(R.id.buttonCancelCreateCard);

        createCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToCreateCard();
            }
        });

        cancelCreateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToMain();
            }
        });

    }

    private void sendToCreateCard() {
        Intent mainIntent = new Intent(NoCardActivity.this, NewCardActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(NoCardActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}