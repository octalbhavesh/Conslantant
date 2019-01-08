package com.octalsoftaware.sage.view.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.octalsoftaware.sage.SageSeekerApplication;
import com.octalsoftaware.sage.constants.I;
import com.octalsoftaware.sage.fcm.FCMInitializationService;
import com.octalsoftaware.sage.network.APIRequest;
import com.octalsoftaware.sage.network.MyApiEndpointInterface;
import com.octalsoftaware.sage.response.ServerImgResponse;
import com.octalsoftaware.sage.util.Utils;
import com.sage.conslantant.android.R;
import com.sage.conslantant.android.databinding.ActivityRegisterTwoBinding;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterTwoActivity extends BaseActivity {
    private ActivityRegisterTwoBinding mBinding;
    private String fname;
    private Uri outputFileUri;
    private File photoFile = null;
    private String userdId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register_two);
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            userdId = getIntent().getExtras().getString("user_id");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setAllHint();
        mBinding.layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23)
                    checkRequestPermission();
                else
                    openImageIntent();
            }
        });
        mBinding.tvRegisterTwoSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickAnimation(mBinding.tvRegisterTwoSubmit);
                EditText etTxt[] = {mBinding.etRegisterTwoFirstName, mBinding.etRegisterTwoLastName, mBinding.etRegisterTwoEmail,
                        mBinding.etRegisterTwoPassword, mBinding.etRegisterTwoConfirmPassword};
                if (isConnectingToInternet(mContext)) {
                    if (checkValidation(etTxt)) {
                        try {
                            if (mBinding.etRegisterTwoPassword.getText().toString().equals(mBinding.etRegisterTwoConfirmPassword.getText().toString())) {
                                callApi(APIRequest.registerTwo(userdId, mBinding.etRegisterTwoFirstName.getText().toString().trim(),
                                        mBinding.etRegisterTwoLastName.getText().toString().trim(),
                                        mBinding.etRegisterTwoEmail.getText().toString().trim(),
                                        mBinding.etRegisterTwoPassword.getText().toString().trim(),
                                        FCMInitializationService.mDeviceToken));
                            } else {
                                errorToast(mContext, getString(R.string.password_does_not_match));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    warningToast(mContext, getString(R.string.no_internet_connection));
                }
            }
        });
    }

    private void setAllHint() {
        mBinding.etRegisterTwoFirstName.setHint(getString(R.string.hint_first_name));
        mBinding.etRegisterTwoLastName.setHint(getString(R.string.hint_last_name));
        mBinding.etRegisterTwoPassword.setHint(getString(R.string.hint_password));
        mBinding.etRegisterTwoConfirmPassword.setHint(getString(R.string.hint_confirm_password));
        mBinding.etRegisterTwoEmail.setHint(getString(R.string.hint_email_address));
    }

    private void checkRequestPermission() {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(mContext), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {
                            android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.CAMERA,
                    }, 1);
        } else {
            openImageIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], int[] grantResults) {
        int i = 0;
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageIntent();
            }
        }
    }

    private void openImageIntent() {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "tripstatus" + File.separator);
        root.mkdirs();
        fname = Utils.getUniqueImageName();
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("path", String.valueOf(outputFileUri));
        editor.apply();
        Log.e("SetUpProfile", "Uri is " + outputFileUri);


        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = mContext.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 0);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, getResources().getString(R.string.select_source));

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, I.YOUR_SELECT_PICTURE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == I.YOUR_SELECT_PICTURE_REQUEST_CODE) {
                selectImage(data);
            }
        }
    }

    public void selectImage(Intent data) {
        final boolean isCamera;
        if (data == null) {
            isCamera = true;
        } else {
            final String action = data.getAction();
            if (action == null) {
                isCamera = false;
            } else {
                isCamera = action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
            }
        }

        Uri selectedImageUri;
        if (isCamera) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            String value = prefs.getString("path", "error");
            selectedImageUri = Uri.parse(value);
        } else {
            selectedImageUri = data.getData();
        }

        if (selectedImageUri == null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            String value = prefs.getString("path", "error");
            selectedImageUri = Uri.parse(value);
        }

        Bitmap bitmap = null;
        Log.d("SetUpProfile", "Uri cropped is " + outputFileUri);
        bitmap = Utils.getBitmap(mContext, selectedImageUri);

        if (bitmap != null) {
            try {
                Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                photoFile = new File(getRealPathFromURI(tempUri));
            } catch (Exception e) {
                e.printStackTrace();
            }
            // photoFile = new File(Utils.getPath(mContext, selectedImageUri));
            mBinding.ivRegisterUser.setImageBitmap(bitmap);
        }


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void callApi(RequestBody requestBody) {
        showProgress(mContext);
        Map<String, RequestBody> map = new HashMap<>();
        MyApiEndpointInterface myApiEndpointInterface = SageSeekerApplication.
                getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ServerImgResponse> call = null;
        MultipartBody.Part profileFile;
        map.put("data", requestBody);
        if (photoFile == null) {
            call = myApiEndpointInterface.register_two(map);
        } else {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), photoFile);
            profileFile = MultipartBody.Part.createFormData("user_image", photoFile.getName(), requestFile);
            call = myApiEndpointInterface.register_two(map, profileFile);
        }
        call.enqueue(new Callback<ServerImgResponse>() {
            @Override
            public void onResponse(Call<ServerImgResponse> call, Response<ServerImgResponse> response) {
                try {
                    hideProgress();
                  /*  String ss = response.body().string();
                    String ss1 = ss;*/
                    if (response.body().getStatus() == getResources().getInteger(R.integer.response_success)) {
                        sucessToast(mContext, response.body().getMessage());
                        startActivity(new Intent(RegisterTwoActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        errorToast(mContext, response.body().getMessage());
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ServerImgResponse> call, Throwable t) {
                hideProgress();
            }
        });

    }
}
