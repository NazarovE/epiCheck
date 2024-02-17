package com.example.appfond;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UnLoginProfileViewActivity extends AppCompatActivity {

    private Button btCrAcc;
    private Button btCanc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlogin_profileview);

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