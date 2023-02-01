package com.vicin.ui.dashboard;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vicin.MainActivity;
import com.vicin.databinding.FragmentDashboardBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    Button btnStartCamera;
    ImageView imageView;
    String currentPhotoPath;
    File photoFile = null;
    Uri photoURI;
    Bitmap bMap = null;
    Bitmap mutableBitmap = null;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        //File file = new File(path, "DemoPicture.jpg");

        //File storageDir = Context#getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                path      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        Date date = new Date();
//        DateFormat df = new SimpleDateFormat("-mm-ss");
//
//        String newPicFile = df.format(date) + ".jpg";
//        String outPath = Environment.getExternalStorageDirectory() + "/" + newPicFile;
//        File outFile = new File(outPath);
//
//        Uri outuri = Uri.fromFile(outFile);
//        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);

        // Create the File where the photo should go
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File

        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            photoURI = FileProvider.getUriForFile(getActivity(),
                    "com.vicin.provider",
                    photoFile);
            Log.i("### photoURI ", photoURI.getPath());
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            galleryActivityResultLauncher.launch(takePictureIntent);
        }

        try {
            //startActivity(takePictureIntent);
            //galleryActivityResultLauncher.launch(takePictureIntent);
            //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
            Log.i("###", "Exception in camera intent");
        }
    }

    void shareImage() {
        Intent intent = new Intent(Intent.ACTION_SEND);

        String title = "Sharing image ..";
        intent.putExtra(Intent.EXTRA_STREAM, photoURI);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_TEXT, "blah blah");

        Intent chooser = Intent.createChooser(intent, title);

        try {
            startActivity(chooser);
        } catch (ActivityNotFoundException e) {
            // Define what your app should do if no activity can handle the intent.
        }
    }

    void saveImage() {

        String imageFileName = "JPEG_";

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageTemp = null;

        try {
            imageTemp = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    path      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

//        File myDir=new File("/sdcard/saved_images");
//        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
//        String fname = "Image-"+ n +".jpg";
//        File file = new File (myDir, fname);
//        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(imageTemp);

            // NEWLY ADDED CODE STARTS HERE [
            Canvas canvas = new Canvas(mutableBitmap);

            Paint paint = new Paint();
            paint.setColor(Color.WHITE); // Text Color
            paint.setTextSize(72); // Text Size
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
            // some more settings...

            canvas.drawBitmap(mutableBitmap, 0, 0, paint);
            canvas.drawText("Testing blah blah...", 100, 100, paint);
            // NEWLY ADDED CODE ENDS HERE ]

            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //here we will handle the result of our intent
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //image picked
                        //imageView.setImageURI(photoURI);

                        bMap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                        mutableBitmap = bMap.copy(Bitmap.Config.ARGB_8888, true);
                        //saveImage();
                        shareImage();
                        imageView.setImageBitmap(mutableBitmap);

                    } else {
                        //cancelled
                        //Toast.makeText(MainActivity.this, "Cancelled...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final TextView textView = binding.textDashboard;
        //dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        btnStartCamera = binding.btnStartCamera;
        imageView = binding.imageView;

        btnStartCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("###", "inside button click");
                dispatchTakePictureIntent();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
