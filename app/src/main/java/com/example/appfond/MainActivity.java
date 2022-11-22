package com.example.appfond;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    public static String currentUser;
    public static Integer User_id;
    private Toolbar mainToolbar;
    private FloatingActionButton addPostBtn;
    private BottomNavigationView mainbottomNav;
    private HomeFragment homeFragment;
    private AboutFragment aboutFragment;
    private  ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);

        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("Фонд Содружество");

        //main menu
        mainbottomNav = findViewById(R.id.mainBottomNav);

        //fragments
        homeFragment = new HomeFragment();
        aboutFragment = new AboutFragment();
        profileFragment = new ProfileFragment();



        replaceFragment(homeFragment);

        mainbottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.bottom_action_home:
                        replaceFragment(homeFragment);
                        return true;
                    case R.id.bottom_action_about:
                        replaceFragment(aboutFragment);
                        return true;
                    case R.id.bottom_action_profile:
                        replaceFragment(profileFragment);
                        return true;
                    default: return false;
                }
            }
        });

        addPostBtn = findViewById(R.id.add_post_btn);
        addPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newPostIntent = new Intent(MainActivity.this, NewPostActivity.class);
                startActivity(newPostIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sh = getSharedPreferences("MyAppFondSettings", Context.MODE_PRIVATE);
        // The value will be default as empty string because for
        // the very first time when the app is opened, there is nothing to show
        String isLogin = sh.getString("current_email", "");
        //int a = sh.getInt("age", 0);
        if (isLogin.equals("")) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else {
            //replaceFragment(homeFragment);
            currentUser = isLogin;
           // Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
           // startActivity(setupIntent);
           // finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(){}
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout_btn:
                //logout code
                currentUser = null;
                SaveSettings(true);
                sendToLogin();
                return true;

            case R.id.action_profile_btn:
                Intent profileIntent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(profileIntent);
                //finish();

            default: return false;
        }

    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    public void SaveSettings (Boolean isOut) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppFondSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        if (isOut == false) {
            myEdit.putString("current_email", MainActivity.currentUser);
        } else {
            myEdit.putString("current_email", null);
        }
        myEdit.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();
    }

}