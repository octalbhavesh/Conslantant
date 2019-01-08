package com.octalsoftaware.sage.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.TextView;

import com.sage.conslantant.android.R;
import com.valdesekamdem.library.mdtoast.MDToast;

import java.io.InputStream;
import java.util.Random;

public class Utils {
    private static Dialog dialog;

    /*public static void showProgress(Context mContext) {
        dialog = new Dialog(mContext, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_progress_dialog);
        dialog.setCancelable(false);
        dialog.show();
    }

    public static void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }*/

    static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static void clickAnimation(TextView tv) {
        AlphaAnimation animation1 = new AlphaAnimation(0.2f, 1.0f);
        animation1.setDuration(1000);
        animation1.setStartOffset(5000);
        animation1.setFillAfter(true);
        tv.startAnimation(animation1);
    }
    public static String getUniqueImageName() {
        //will generate a random num
        //between 15-10000
        Random r = new Random();
        int num = r.nextInt(10000 - 15) + 15;
        String fileName = "img_" + num + ".png";
        return fileName;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }


        }
        // MediaStore (and general)

        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return "Not Found";
    }
    public static Bitmap getBitmap(Context context, Uri u) {

        Uri uri = u;
        InputStream in = null;
        try {
            final int IMAGE_MAX_SIZE = 60000; // around 300kb
            in = context.getContentResolver().openInputStream(uri);

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, o);
            in.close();


            int scale = 1;
            while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                    IMAGE_MAX_SIZE) {
                scale++;
            }
            Log.d("Test", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

            Bitmap b = null;
            in = context.getContentResolver().openInputStream(uri);
            if (scale > 1) {
                scale--;
                // scale to max possible inSampleSize that still yields an image
                // larger than target
                o = new BitmapFactory.Options();
                o.inSampleSize = scale;
                b = BitmapFactory.decodeStream(in, null, o);

                // resize to desired dimensions
//                int height = b.getHeight();
//                int width = b.getWidth();
                int height = 340;
                int width = 340;

                Log.d("Test", "1th scale operation dimenions - width: " + width + ", height: " + height);

                double y = Math.sqrt(IMAGE_MAX_SIZE
                        / (((double) width) / height));
                double x = (y / height) * width;

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                        (int) y, true);
                b.recycle();
                b = scaledBitmap;

                System.gc();
            } else {
                b = BitmapFactory.decodeStream(in);
            }
            in.close();

          /*  Log.d("Test", "bitmap size - width: " + b.getWidth() + ", height: " +
                    b.getHeight());*/
            return b;
        } catch (Exception e) {
            Log.e("Test", e.getMessage(), e);
            return null;
        }
    }
    public static void customeFont(Context context, TextView tv) {
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "FaktPro-Normal.ttf");
        tv.setTypeface(custom_font);
    }

    public static boolean checkValidation(EditText[] etTxt) {
        boolean status = false;
        for (int i = 0; i < etTxt.length; i++) {
            EditText et = etTxt[i];
           // String ss = String.valueOf(et.getHint().toString());
            if (et.getText().toString().trim().equalsIgnoreCase("")) {
                et.setError("Enter " + et.getHint() + "");
                et.setFocusable(true);
                status = false;
            } else {
                // email vaidation
                if (et.getHint().toString().trim().equalsIgnoreCase("email") ||
                        et.getHint().toString().trim().equalsIgnoreCase("email address")) {
                    if (!et.getText().toString().trim().matches(emailPattern)) {
                        et.setError("Enter " + "valid email" + "");
                        et.setFocusable(true);
                        status = false;
                    }
                }
                // phone number validation
                else if (et.getHint().toString().trim().equalsIgnoreCase("mobile number")
                        || et.getHint().toString().trim().equalsIgnoreCase("phone number")
                        || et.getHint().toString().trim().equalsIgnoreCase("phone")
                        || et.getHint().toString().trim().equalsIgnoreCase("mobile")) {
                    if (et.getText().toString().trim().length() < 6) {
                        et.setError("Enter " + "valid phone number" + "");
                        et.setFocusable(true);
                        status = false;
                    } else {
                        status = true;
                    }
                } else {
                    status = true;
                }
            }
        }
        return status;
    }

    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
        }
        return false;
    }

    public static void sucessToast(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_SUCCESS);
        mdToast.show();
    }

    public static void errorToast(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_ERROR);
        mdToast.show();
    }

    public static void warningToast(Context context, String message) {
        MDToast mdToast = MDToast.makeText(context, message, MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
        mdToast.show();
    }
    public static void hideProgress() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
    public static  void showProgress(Context mContext) {
        dialog = new Dialog(mContext, android.R.style.Theme_Translucent);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custome_progress_dialog);
        dialog.setCancelable(false);
        dialog.show();
    }
}
