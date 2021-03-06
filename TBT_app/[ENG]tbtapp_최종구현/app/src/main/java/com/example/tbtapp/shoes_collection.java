package com.example.tbtapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class shoes_collection extends AppCompatActivity {

    EditText edtKind, edtName, edtPrice, edtSize, edtAngle;
    Button btnChoose, btnAdd, btnList;
    ImageView imageView2;

    final int REQUEST_CODE_GALLERY = 999;

    public static DBHelper_Image  DBHelper_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoes_collection);

        init();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        shoes_collection.this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try{
                    DBHelper_image.insertData(edtKind.getText().toString().trim(),
                            edtName.getText().toString().trim(),
                            edtPrice.getText().toString().trim(),
                            edtSize.getText().toString().trim(),
                            edtAngle.getText().toString().trim(),
                            imageViewToByte(imageView2));
                    Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
                    edtKind.setText("Kind");
                    edtName.setText("Name");
                    edtPrice.setText("Price");
                    edtSize.setText("Size");
                    edtAngle.setText("Angle");
                    imageView2.setImageResource(R.mipmap.ic_launcher);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(shoes_collection.this, FootList.class);
                startActivity(intent);
            }
        });

    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

       if(requestCode == REQUEST_CODE_GALLERY) {
           if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               Intent intent = new Intent(Intent.ACTION_PICK);
               intent.setType("image/*");
               startActivityForResult(intent, REQUEST_CODE_GALLERY);
           }
           else {
               Toast.makeText(getApplicationContext(), "You don't permission to access file location!", Toast.LENGTH_SHORT).show();
           }
           return;
       }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView2.setImageBitmap(bitmap);
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void init() {
        edtKind = (EditText) findViewById(R.id.editKind);
        edtName = (EditText) findViewById(R.id.editName);
        edtPrice = (EditText) findViewById(R.id.edtPrice);
        edtSize = (EditText) findViewById(R.id.edtSize);
        edtAngle = (EditText) findViewById(R.id.edtAngle);

        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnList = (Button) findViewById(R.id.btnList);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
    }
}
