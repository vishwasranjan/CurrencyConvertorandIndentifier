package com.project.currencyconvertorandindentifier;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.net.URI;

public class MainActivity extends AppCompatActivity {
    ImageView camera_icon;
    Button scan_image_button;
    boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera_icon=findViewById(R.id.camera_icon);
        scan_image_button=findViewById(R.id.scan_image_button);



        camera_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImage();
            }
        });

        scan_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag==false)
                {
                    Toast.makeText(MainActivity.this, "Select an Image", Toast.LENGTH_SHORT).show();
                }
                else {
                    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppCompatAlertDialogStyle);
                    progressDialog.setMessage("Scanning");
                    progressDialog.setCancelable(true);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.show();
                }
            }
        });
    }

    private void PickImage() {
        ImagePicker.with(this)
                  .crop()	    			//Crop image(Optional), Check Customization for more option
//                .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri=data.getData();
        camera_icon.setImageURI(uri);
        flag=true;

    }
}