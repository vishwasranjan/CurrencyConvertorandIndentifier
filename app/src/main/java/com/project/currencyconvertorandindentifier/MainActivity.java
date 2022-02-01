package com.project.currencyconvertorandindentifier;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.sql.Ref;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ImageView camera_icon;
    Button scan_image_button;
    boolean flag=false;
    FirebaseStorage storage;
    StorageReference storageReference;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        camera_icon=findViewById(R.id.camera_icon);
        scan_image_button=findViewById(R.id.scan_image_button);
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        progressDialog = new ProgressDialog(MainActivity.this, R.style.AppCompatAlertDialogStyle);



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

                    progressDialog.setMessage("Scanning");
                    progressDialog.setCancelable(true);
                    progressDialog.setTitle("Please Wait");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    UploadImagetoServer();


                    //CODES FOR IMAGE PROCESSING



                }
            }
        });
    }

    private void UploadImagetoServer() {
        String randomkey=UUID.randomUUID().toString();
        StorageReference mountainsRef = storageReference.child("Images").child(randomkey);



        camera_icon.setDrawingCacheEnabled(true);
        camera_icon.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) camera_icon.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(MainActivity.this, "Image is uploaded successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void PickImage() {
        ImagePicker.with(this)
                  .crop()	    			//Crop image(Optional), Check Customization for more option
                 .compress(1024)			//Final image size will be less than 1 MB(Optional) Max size 1MB
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