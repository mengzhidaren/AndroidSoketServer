package com.yyl.server;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private SimpleHttpServer shs;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        WebConfiguration wc = new WebConfiguration();
        wc.setPort(8088);
        wc.setMaxParallels(50);
        shs = new SimpleHttpServer(wc);
        shs.registerResourceHandler(new ResourceInAssetsHandler(this));
        shs.registerResourceHandler(new UploadImageHandler(this) {
            @Override
            public void onImageLoaded(final String path) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }
        });
        shs.registerResourceHandler(new GetPostHandler());
        shs.startAsync();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shs.stopAsync();
    }
}
