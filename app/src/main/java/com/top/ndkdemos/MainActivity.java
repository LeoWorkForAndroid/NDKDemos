package com.top.ndkdemos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import org.opencv.utils.OpenCVUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.bbb)).getBitmap();
        int w = bitmap.getWidth(), h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int [] resultPixes= OpenCVUtils.gray(pix,w,h);
        Bitmap result = Bitmap.createBitmap(w,h, Bitmap.Config.RGB_565);
        result.setPixels(resultPixes, 0, w, 0, 0,w, h);

        ImageView img = (ImageView)findViewById(R.id.iv);
        img.setImageBitmap(result);
    }



}
