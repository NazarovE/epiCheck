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
    public static String User_id;
    public static String is_super;
    public static String fullname_user;
    public static String user_city;
    public static String image_link;
    public static String count_cards;

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
        getSupportActionBar().show();
        getSupportActionBar().getCustomView();
        getSupportActionBar().getDisplayOptions();


        //main menu
        mainbottomNav = findViewById(R.id.mainBottomNav);

        //fragments
        homeFragment = new HomeFragment();
        aboutFragment = new AboutFragment();
        profileFragment = new ProfileFragment();





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
                    default: replaceFragment(homeFragment);
                                 return true;
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

        replaceFragment(homeFragment);

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
            /*Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();*/
            sendToLogin();
        } else {
            //replaceFragment(homeFragment);
            currentUser = isLogin;
            user_city = sh.getString("city","");
            count_cards = sh.getString("count_cards","0");
            fullname_user = sh.getString("fullname","");
            is_super = sh.getString("super","0");
            image_link = sh.getString("image","");
           // Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
           // startActivity(setupIntent);
           // finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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