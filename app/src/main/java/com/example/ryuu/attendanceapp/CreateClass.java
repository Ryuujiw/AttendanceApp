package com.example.ryuu.attendanceapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.objects.Classes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateClass extends AppCompatActivity {
    EditText text,et_location;
    Button gen_btn,btn_gotoClass;
    TextView tv_generatedURL;
    ImageView image;
    String text2Qr;
    Classes classes;
    Bitmap bitmap;
    StorageReference mStorageRef;
    DatabaseReference mDataRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_class);
        text = findViewById(R.id.et_className);
        et_location = findViewById(R.id.et_location);
        tv_generatedURL = findViewById(R.id.et_generatedurl);
        image = findViewById(R.id.image);
        gen_btn = findViewById(R.id.btn_generate);
        gen_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text2Qr = text.getText().toString().trim()+"/generatedQR/"+et_location.getText().toString().trim();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try{
                    //generate barcode images from classname and location
                    BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);
                    tv_generatedURL.setText(text2Qr);
                    //Generate start Time when generate button is clicked
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    String startTime = format.format(calendar.getTime());
                    //new classes created
                    classes = new Classes(text.getText().toString(),text2Qr,et_location.getText().toString());
                    classes.setStartTime(startTime);
                    //upload classes into firebase
                    mDataRef = FirebaseDatabase.getInstance().getReference("classes_uploads");
                    mDataRef.setValue(classes);

                    //upload QR image into firebase
                    mStorageRef = FirebaseStorage.getInstance().getReference("qruploads");
                    StorageReference childRef = mStorageRef.child("qrImage.bmp");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();
                    UploadTask uploadTask = childRef.putBytes(data);




                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });
        btn_gotoClass = findViewById(R.id.btn_backtoclass);
        btn_gotoClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateClass.this,AttendanceFragment.class);
                startActivity(intent);
            }
        });



    }
}
