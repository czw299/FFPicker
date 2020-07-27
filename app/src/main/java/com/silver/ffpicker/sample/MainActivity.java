package com.silver.ffpicker.sample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.silver.ffpicker.R;
import com.silver.ffpicker.FFPicker;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button1, button2;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            },1);
        }

        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FFPicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(123)
                        .start();
            }
        });

        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FFPicker()
                        .withActivity(MainActivity.this)
                        .withRequestCode(123)
                        .withChooseFolderMode(FFPicker.Companion.getCHOOSE_FOLDER())
                        .start();
            }
        });
        textView = findViewById(R.id.textView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 123:
                try{
                    List<String> list = data.getStringArrayListExtra("paths");
                    if(list == null || list.size() == 0){
                        break;
                    }
                    textView.setText(list.get(0));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
        }
    }
}