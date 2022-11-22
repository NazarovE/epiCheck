package com.example.appfond;

import static java.lang.System.in;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity<fun, resultCode, requestCode> extends AppCompatActivity {

    private CircleImageView setupImage;
    private Uri mainImageUri = null;
    private EditText setupName;
    private Button setupBtn;
    private ProgressBar setupProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Toolbar setupToolbar = findViewById(R.id.septuToolbar);
        setSupportActionBar(setupToolbar);
        getSupportActionBar().setTitle("Настройки");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setupImage = findViewById(R.id.profile_image);
        setupName = findViewById(R.id.setupNameField);
        setupBtn = findViewById(R.id.setup_btn);
        setupProgress = findViewById(R.id.setup_progressBar);

        setupName.setText(MainActivity.currentUser);

        RequestOptions placeholderRequest = new RequestOptions();
        placeholderRequest.placeholder(R.drawable.default_profile);

        String image = "https://vhost268072.cpsite.ru/appfond/images/profile/28/profile215.jpg";
        Glide.with(this).setDefaultRequestOptions(placeholderRequest).load(image).into(setupImage);
        mainImageUri = Uri.parse(image);

        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = setupName.getText().toString();

                if (!TextUtils.isEmpty(user_name) && mainImageUri != null){
                    //https request
                    setupProgress.setVisibility(View.VISIBLE);

                    if (0 == 0) {
                        Map<String, String> userMap = new HashMap<>();
                        userMap.put("name", user_name);
                        userMap.put("image", mainImageUri.toString());
                        Toast.makeText(SetupActivity.this, "Изображение загружено", Toast.LENGTH_SHORT).show();
                        sendToMain();
                    } else {
                        String error = "error https";
                        Toast.makeText(SetupActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    setupProgress.setVisibility(View.INVISIBLE);
                }
            }
        });

        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(SetupActivity.this, "Permission denided", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }else{
                        Toast.makeText(SetupActivity.this, "You already have permission", Toast.LENGTH_SHORT).show();
                       BringimagePicker();
                    }

                }else{
                    BringimagePicker();
                }
            }
        });
    }

    private void BringimagePicker() {
        ImagePicker.with(SetupActivity.this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            mainImageUri = data.getData();
            // Use Uri object instead of File to avoid storage permissions
            setupImage.setImageURI(mainImageUri);
            //load image to back
            //...
            //
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(SetupActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}