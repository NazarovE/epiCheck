package com.example.appfond;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;


import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private CircleImageView setupImage;
    private Uri mainProfileUri = null;
    private EditText setupName;
    private Button setupBtn;
    private ProgressBar setupProgress;
    public static final int PICK_IMAGE = 1;
    private ImageView profileImage;

    public ProfileFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }



    private void BringimagePicker() {
        /*ImagePicker.with(getActivity().getApplicationContext().this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();*/

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            //TODO: action
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        startActivity(mainIntent);
        //finish();
    }

    private void sendToProfile() {
        Intent profileIntent = new Intent(getActivity().getApplicationContext(), SetupActivity.class);
        startActivity(profileIntent);
        //finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        sendToProfile();
    }
}