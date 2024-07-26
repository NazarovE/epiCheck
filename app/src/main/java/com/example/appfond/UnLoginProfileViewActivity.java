package com.example.appfond;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class UnLoginProfileViewActivity extends AppCompatActivity {

    private Button btCrAcc;
    private Button btCanc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlogin_profileview);

        ImageView imageView = findViewById(R.id.imageLogoUnLogin);

        // Установка изображения с закругленными углами с помощью Glide
        RequestOptions requestOptions = new RequestOptions()
                .transform(new RoundedCorners(50)); // Радиус закругления углов в пикселях

        Glide.with(this)
                .load(R.drawable.epicheck_logo) // Замените на ваш ресурс изображения
                .apply(requestOptions)
                .into(imageView);

        btCrAcc = findViewById(R.id.buttonCreateAcc);
        btCanc = findViewById(R.id.buttonCancel);

        //создать
        btCrAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendToCreateAcc();
            }
        });

        //отмена
        btCanc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToPosts();
            }
        });

    }

    private void sendToCreateAcc() {
        Intent mainIntent = new Intent(UnLoginProfileViewActivity.this, RegisterActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void sendToPosts() {
        Intent mainIntent = new Intent(UnLoginProfileViewActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

}