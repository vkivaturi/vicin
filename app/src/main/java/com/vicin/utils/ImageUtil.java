package com.vicin.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.util.Log;

import com.vicin.model.LocationData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ImageUtil {

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        Log.i("### Image time stamp", timeStamp);
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                path      /* directory */
        );

        return image;
    }

    public static Bitmap addTextToImage(File photoFile, LocationData locationData) {

        //Convert the image file into a bitmap first. Then make the bitmap mutable so that we can add text
        Bitmap bMap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
        Bitmap mutableBitmap = bMap.copy(Bitmap.Config.ARGB_8888, true);

        //Create a temporary file to which the output bitstream can be written
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

        //Create text strings with required details to be added to the image
        //String eventTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm a").format(new Date());
        //
        int w = mutableBitmap.getWidth();
        int h = mutableBitmap.getHeight();
        //
        //Use input bitmap, output temp file. Canvas and Paint are used to write the text at desired location
        try {
            FileOutputStream out = new FileOutputStream(imageTemp);

            Canvas canvas = new Canvas(mutableBitmap);

            Paint paint = new Paint();
            paint.setColor(Color.RED); // Text Color
            paint.setTextSize(80); // Text Size
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // Text Overlapping Pattern
            canvas.drawBitmap(mutableBitmap, 0, 0, paint);
            canvas.drawText(locationData.getTimeStamp(), 100, 100, paint);
            canvas.drawText(locationData.getLocationLine1(), 100, 200, paint);
            canvas.drawText(locationData.getLatitude() + "," + locationData.getLongitude(), 100, 300, paint);

            mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mutableBitmap;
    }
}
