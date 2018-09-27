package ru.skoltech.smartbrace;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private ImageButton photo_picker;
    private ProgressBar pbar;

    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private Intent pictureActionIntent = null;
    Bitmap bitmap;

    String selectedImagePath;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Activity mActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        pbar = findViewById(R.id.update_progress);

        photo_picker = (ImageButton) findViewById(R.id.take_photo_btn);
        photo_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });
    }

    private void startDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(mActivity);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(
                                pictureActionIntent,
                                GALLERY_PICTURE);

                    }
                });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment
                                .getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(f));

                        startActivityForResult(intent,
                                CAMERA_REQUEST);

                    }
                });
        myAlertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {

            File f = new File(Environment.getExternalStorageDirectory()
                    .toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals("temp.jpg")) {
                    f = temp;
                    break;
                }
            }

            if (!f.exists()) {

                Toast.makeText(getBaseContext(),

                        "Error while capturing image", Toast.LENGTH_LONG)

                        .show();

                return;

            }

            try {

//                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
//
//                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);
//
//                int rotate = 0;
//                try {
//                    ExifInterface exif = new ExifInterface(f.getAbsolutePath());
//                    int orientation = exif.getAttributeInt(
//                            ExifInterface.TAG_ORIENTATION,
//                            ExifInterface.ORIENTATION_NORMAL);
//
//                    switch (orientation) {
//                        case ExifInterface.ORIENTATION_ROTATE_270:
//                            rotate = 270;
//                            break;
//                        case ExifInterface.ORIENTATION_ROTATE_180:
//                            rotate = 180;
//                            break;
//                        case ExifInterface.ORIENTATION_ROTATE_90:
//                            rotate = 90;
//                            break;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                Matrix matrix = new Matrix();
//                matrix.postRotate(rotate);
//                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
//                        bitmap.getHeight(), matrix, true);

                // process image

                showProgress(true);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // do nothing
                }

                showProgress(false);

                Intent intent = new Intent(mActivity, ProgressActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {

//                Uri selectedImage = data.getData();
//                String[] filePath = { MediaStore.Images.Media.DATA };
//                Cursor c = getContentResolver().query(selectedImage, filePath,
//                        null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                selectedImagePath = c.getString(columnIndex);
//                c.close();
//
//                bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
//                // preview image
//                bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);

                // process image

                showProgress(true);

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // do nothing
                }

                showProgress(false);

                Intent intent = new Intent(mActivity, ProgressActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            photo_picker.setVisibility(show ? View.GONE : View.VISIBLE);
            photo_picker.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    photo_picker.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            pbar.setVisibility(show ? View.VISIBLE : View.GONE);
            pbar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    pbar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            pbar.setVisibility(show ? View.VISIBLE : View.GONE);
            photo_picker.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}