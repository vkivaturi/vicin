package com.vicin.ui.dashboard;

import android.Manifest;
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
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.vicin.MainActivity;
import com.vicin.databinding.FragmentDashboardBinding;
import com.vicin.model.LocationData;
import com.vicin.utils.ImageUtil;
import com.vicin.utils.LocationUtil;

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
    FusedLocationProviderClient fusedLocationClient;
    LocationData locationData = new LocationData();

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        try {
            photoFile = ImageUtil.createImageFile();
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

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //here we will handle the result of our intent
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mutableBitmap = ImageUtil.addTextToImage(photoFile, locationData);
                        //shareImage();
                        imageView.setImageBitmap(mutableBitmap);

                    } else {
                        //cancelled
                        //Toast.makeText(MainActivity.this, "Cancelled...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                                Log.i("###", "Precise location access granted.");
                                LocationUtil.getLocationCoords(fusedLocationClient, getActivity(), locationData);
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                                Log.i("###", "Only approximate location access granted.");
                                LocationUtil.getLocationCoords(fusedLocationClient, getActivity(), locationData);
                            } else {
                                // No location access granted.
                                Log.i("###", "No location access granted.");
                            }
                        }
                );

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnStartCamera = binding.btnStartCamera;
        imageView = binding.imageView;

        btnStartCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("###", "inside button click");
                //Set timestamp for location data
                locationData.setTimeStamp(new SimpleDateFormat("dd-MMM-yyyy HH:mm a").format(new Date()));

                locationPermissionRequest.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
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
