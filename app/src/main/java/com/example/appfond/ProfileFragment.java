package com.example.appfond;

import static android.content.Context.MODE_PRIVATE;

import static com.example.appfond.MainActivity.nameSettings;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
//import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


/*import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;*/

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

//import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    //private Uri selectedImageUri;
    //private EditText setupName;
    //private Button setupBtn, sendToDiag;
    private ProgressBar setupProgress;
    public static final int PICK_IMAGE = 1;
    private ImageView profileImage;
    private boolean isFirstLaunch = true;
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Bitmap to get image from gallery
    //private Bitmap bitmap;
    private String encodedImage;
    //private Button btnUploadImg;
    private TextView labelName;
    //Uri to store the image uri
    private Uri filePath;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileImage = view.findViewById(R.id.profile_image_value);
        profileImage.setOnClickListener(v -> startImagePicker());
    }

    private void startImagePicker() {
        if (getActivity() == null || getActivity().isFinishing()) return;

        if (isFirstLaunch) {
            // Ждем завершения отрисовки для первого запуска
            profileImage.post(() -> {
                if (isAdded() && getActivity() != null) {
                    launchImagePicker();
                }
            });
            isFirstLaunch = false;
        } else {
            launchImagePicker();
        }
    }

    private void launchImagePicker() {
        ImagePicker.with(getActivity())
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.User_id.equals("0")) {
            Intent mainIntent = new Intent(getActivity().getApplicationContext(), UnLoginProfileViewActivity.class);
            //MainActivity.from_add = 1;
            startActivity(mainIntent);
        }
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        boolean getForm;
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        /*labelName = view.findViewById(R.id.labelFullNameProfile);

        if (getArguments() != null) {
            String text = getArguments().getString("label_text");
            labelName.setText(text);
        }*/


        if (MainActivity.User_id.equals("0")) {
            Intent mainIntent = new Intent(getActivity().getApplicationContext(), UnLoginProfileViewActivity.class);
            startActivity(mainIntent);
            getForm = false;
        } else {

            getForm = true;
            setupProgress = view.findViewById(R.id.profileProgressBar);
            TextView fullname = view.findViewById(R.id.labelFullNameProfile);
            TextView city = view.findViewById(R.id.labelCityValue);
            TextView email = view.findViewById(R.id.labelEmailProfileValue);
            profileImage = view.findViewById(R.id.profile_image_value);
            //sendToDiag = view.findViewById(R.id.buttonProfToDiag);
            //private Bitmap bm;
            Button btSendFB = view.findViewById(R.id.buttonSendFB);

            ImageView edCity = view.findViewById(R.id.buttonEditCity);
            edCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendToEdCity();
                }
            });

            ImageView edName = view.findViewById(R.id.buttonEditNamePr);
            edName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendToEdName();
                }
            });
            Spinner spinner = view.findViewById(R.id.spinnerLang);

            // Создаем адаптер из массива
            ArrayAdapter<CharSequence> adapter_lang = ArrayAdapter.createFromResource(
                    getActivity(),
                    R.array.spinner_items,
                    R.layout.spinner_item
            );
            adapter_lang.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter_lang);

            switch (GlobalVariables.languageApp){
                case "en":
                    spinner.setSelection(0, false);
                    break;
                case "ru":
                    spinner.setSelection(1, false);
                    break;
                case "tr":
                    spinner.setSelection(2, false);
                    break;
                case "el":
                    spinner.setSelection(3, false);
                    break;
                case "de":
                    spinner.setSelection(4, false);
                    break;
                case "fr":
                    spinner.setSelection(5, false);
                    break;
                case "it":
                    spinner.setSelection(6, false);
                    break;
                case "es":
                    spinner.setSelection(7, false);
                    break;
                case "pt":
                    spinner.setSelection(8, false);
                    break;
                case "hu":
                    spinner.setSelection(9, false);//@
                    break;
                case "nb":
                    spinner.setSelection(10, false);
                    break;
                case "sv":
                    spinner.setSelection(11, false);
                    break;
                case "fi":
                    spinner.setSelection(12, false);
                    break;
                case "da":
                    spinner.setSelection(13, false);
                    break;
                case "zh":
                    spinner.setSelection(14, false);
                    break;
                case "nl":
                    spinner.setSelection(15, false);
                    break;
                case "ro":
                    spinner.setSelection(16, false);
                    break;
                case "ja":
                    spinner.setSelection(17, false);
                    break;
                case "ko":
                    spinner.setSelection(18, false);
                    break;
                case "pl":
                    spinner.setSelection(19, false);
                    break;
                default:
                    spinner.setSelection(1, false);
                    break;
            }

            // Обработчик выбора
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();

                /*if (GlobalVariables.isFirstSelection2 == 0) {
                    GlobalVariables.isFirstSelection2++;
                } else if (GlobalVariables.isFirstSelection2 == 1) {
                    GlobalVariables.isFirstSelection2++;
                } else {*/

                //if (GlobalVariables.isFirstSelection) {
                    //GlobalVariables.isFirstSelection = false;
                /*} else if (GlobalVariables.isFirstSelection2 == 1) {
                    GlobalVariables.isFirstSelection2++;*/
                //} else {



                    /*Toast.makeText(getActivity(), String.format("Выбрано: %s %d", selectedItem, position),
                                Toast.LENGTH_SHORT).show();*/

                        String lang = "en";
                        switch (position) {
                            case 0:
                                lang = "en";
                                break;
                            case 1:
                                lang = "ru";
                                break;
                            case 2:
                                lang = "tr";
                                break;
                            case 3:
                                lang = "el";
                                break;
                            case 4:
                                lang = "de";
                                break;
                            case 5:
                                lang = "fr";
                                break;
                            case 6:
                                lang = "it";
                                break;
                            case 7:
                                lang = "es";
                                break;
                            case 8:
                                lang = "pt";
                                break;
                            case 9:
                                lang = "hu";
                                break;
                            case 10:
                                lang = "nb";
                                break;
                            case 11:
                                lang = "sv";
                                break;
                            case 12:
                                lang = "fi";
                                break;
                            case 13:
                                lang = "da";
                                break;
                            case 14:
                                lang = "zh";
                                break;
                            case 15:
                                lang = "nl";
                                break;
                            case 16:
                                lang = "ro";
                                break;
                            case 17:
                                lang = "ja";
                                break;
                            case 18:
                                lang = "ko";
                                break;
                            case 19:
                                lang = "pl";
                                break;
                            default:
                                setAppLocale(getActivity(), "en");
                                break;
                        }

                        setAppLocale(getActivity(), lang);
                        SaveSettings("Language", lang);
                        setLocaleAndRestart(lang);

                    }
               // }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Действие при отсутствии выбора
                }
            });


            /*sendToDiag.setText(MainActivity.count_cards);
            sendToDiag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendToDiag();
                }
            });*/

            btSendFB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent fbIntent = new Intent(getActivity().getApplicationContext(), FeedBackActivity.class);
                    startActivity(fbIntent);
                }
            });

            //set values
            fullname.setText(MainActivity.fullname_user);

            city.setText(MainActivity.user_city);
            email.setText(MainActivity.currentUser);

            HTTPSBase Global = new HTTPSBase();
            String image = Global.URL_ROOT + "/" + GlobalVariables.image_profile;
            //Toast toast = Toast.makeText(getActivity(),"image = " + image,Toast.LENGTH_SHORT);
            //toast.show();
            if (!image.equals(Global.URL_ROOT + "/null")) {
                RequestOptions placeholderRequest = new RequestOptions();
                placeholderRequest.placeholder(R.drawable.default_profile);

                //  bm = BitmapFactory.decodeFile(R.drawable.default_profile);
                //executeMultipartPost();


                Glide.with(this).setDefaultRequestOptions(placeholderRequest).load(image).into(profileImage);
                //private CircleImageView setupImage;
                Uri profileImageUri = Uri.parse(image);
            }


            profileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        openImagePicker();
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }*/

                    //sendToProfile();
                    //sendToImagePicker();
                    // openGallery(SELECT_FILE1);
                /*    Dexter.withActivity(getActivity())
                            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            .withListener(new PermissionListener() {
                                @Override
                                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(Intent.createChooser(intent, getString(R.string.textChooseImage)), 1);
                                }

                                @Override
                                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                    permissionToken.continuePermissionRequest();
                                }
                            }).check();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(getActivity(), "Permission denided", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }else{
                        Toast.makeText(getActivity(), "You already have permission", Toast.LENGTH_SHORT).show();
                        BringimagePicker();
                    }

                }else{
                    BringimagePicker();
                }*/


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        // Для Android 10+ просто открываем выбор изображения (разрешение не нужно)
                        BringimagePicker();
                    } else {
                        // Для старых версий запрашиваем разрешение через Dexter
                        Dexter.withActivity(getActivity())
                                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                .withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {
                                        BringimagePicker();
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {
                                        Toast.makeText(getActivity(), getString(R.string.textPermissionNotGrant), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                        token.continuePermissionRequest();
                                    }
                                }).check();
                    }


                }




            });




        }

        if (getForm) {
            return view;
        } else {
            return null;
        }




    }

    private void sendToEdName() {
        Intent edNameIntent = new Intent(getActivity().getApplicationContext(), EditNameActivity.class);
        startActivity(edNameIntent);
    }

    private void sendToEdCity() {
        Intent edCityIntent = new Intent(getActivity().getApplicationContext(), EditCityActivity.class);
        startActivity(edCityIntent);
    }

    @SuppressLint("NewApi")
    private void setLocaleAndRestart(String languageCode) {
        // Устанавливаем локаль
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        // Обновляем конфигурацию ресурсов
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        // Сохраняем язык в SharedPreferences (для Fragment используем requireContext())
        SharedPreferences prefs = requireContext().getSharedPreferences(nameSettings, Context.MODE_PRIVATE);
        prefs.edit().putString("Language", languageCode).apply();

        //Runtime.getRuntime().exit(0); // Гарантированный перезапуск

        GlobalVariables.isFirstSelection = false;
        //getActivity().finish();
        //System.exit(0);

        AlertDialog alertDialogDel = new AlertDialog.Builder(getActivity())
                //set icon
                .setIcon(R.drawable.warning)
                //set title
                .setTitle(R.string.textAttention)
                //set message
                .setMessage(R.string.textChangeLangMessage)
                //set positive button
                .setPositiveButton(R.string.textYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        //requireActivity().finishAffinity();
                        System.exit(0);
                        //finish();
                    }
                })
                //set negative button
                .setNegativeButton(R.string.textLater, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        //Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                    }
                })
                .show();


            // Перезапускаем родительскую Activity
        /*if (getActivity() != null) {
            getActivity().recreate();
        }*/



    }




    public void setAppLocale(Context context, String languageCode) {
        // Создаем объект Locale для нового языка
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        // Получаем ресурсы и конфигурацию
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();

        // Устанавливаем новую локаль
        config.setLocale(locale);

        // Обновляем конфигурацию
        resources.updateConfiguration(config, resources.getDisplayMetrics());



    }
    //----------------------------------------------------------------------------------------------
    private void uploadImage(){
        HTTPSBase Global = new HTTPSBase();
        StringRequest request_photo = new StringRequest(Request.Method.POST, Global.URL_UPLOAD_IMG_PROFILE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //System.out.println("I'm uploading image...");
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    //System.out.println("response=" + response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
              //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                String tmp_path = null;
                try {
                    tmp_path = jsonObject.getString("path_img");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MainActivity.image_link = tmp_path;
                GlobalVariables.image_profile = tmp_path;
                SaveSettings("image", MainActivity.image_link.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("file",encodedImage);
                params.put("email", MainActivity.currentUser);
                return params;
            }
        };

        // RequestQueue requestQueue = new RequestQueue(this);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request_photo);
    }
    //----------------------------------------------------------------------------------------------
    /*private void BringimagePicker() {
        ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();


        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        setResult(Activity.RESULT_OK);

    }*/

private void BringimagePicker() {
    // Проверяем состояние фрагмента

        ImagePicker.with(getActivity()) // Используем getActivity() вместо requireActivity()
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();

}



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data); // Важно вызывать super


        //System.out.println("res code=" + resultCode);
        if (resultCode == Activity.RESULT_OK) {
            System.out.println("req code=" + requestCode);
            if (requestCode == ImagePicker.REQUEST_CODE) { // Проверяем ваш requestCode
                if (data != null && data.getData() != null) {
                    Uri filePath = data.getData();

                    try {
                        // Вариант 1: Загрузка через Bitmap (для небольших изображений)
                        InputStream inputStream = getActivity().getContentResolver().openInputStream(filePath);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        profileImage.setImageBitmap(bitmap);

                        // Вызываем загрузку на сервер
                        imageStore(bitmap);
                        uploadImage();

                        // ИЛИ Вариант 2: Просто установка URI (более эффективно)
                        // profileImage.setImageURI(filePath);
                        // uploadImage(filePath); // Нужно изменить метод uploadImage

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        //Toast.makeText(getActivity(), "Файл не найден", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //Toast.makeText(getActivity(), "Не удалось выбрать изображение", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Toast.makeText(getActivity(), "Выбор изображения отменён", Toast.LENGTH_SHORT).show();
        }
    }

    private void imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] imageBytes = stream.toByteArray();
        encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    /*private void sendToDiag() {
        Intent mainIntent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        MainActivity.from_add = 1;
        startActivity(mainIntent);
        //finish();
    }*/

    private void sendToProfile() {
        Intent profileIntent = new Intent(getActivity().getApplicationContext(), SetupActivity.class);
        startActivity(profileIntent);
        //finish();
    }

    private void sendToImagePicker() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    public void openGallery(int req_code) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select file to upload "), req_code);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (MainActivity.User_id.equals("0")) {
            Intent mainIntent = new Intent(getActivity().getApplicationContext(), UnLoginProfileViewActivity.class);
            //MainActivity.from_add = 1;
            startActivity(mainIntent);
        }
       // sendToProfile();
    }

    public void SaveSettings (String setting, String value) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(nameSettings, MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        // Storing the key and its value as the data fetched from edittext
        myEdit.putString(setting, value);
        //myEdit.putInt("age", Integer.parseInt(age.getText().toString()));
        myEdit.commit();
    }




}