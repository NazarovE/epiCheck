package com.example.appfond;

import static android.view.Gravity.apply;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.imageLogoSplash);

        // Установка изображения с закругленными углами с помощью Glide
        RequestOptions requestOptions = new RequestOptions()
                .transform(new RoundedCorners(50)); // Радиус закругления углов в пикселях

        Glide.with(this)
                .load(R.drawable.epicheck_logo) // Замените на ваш ресурс изображения
                .apply(requestOptions)
                .into(imageView);

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Закрываем SplashActivity

        // Переход на MainActivity после задержки
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
               finish();
            }
        }, 20000); */// Задержка 3 секунды
    }

}
