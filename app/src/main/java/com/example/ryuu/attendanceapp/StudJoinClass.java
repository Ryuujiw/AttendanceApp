package com.example.ryuu.attendanceapp;

import android.content.Intent;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StudJoinClass extends AppCompatActivity {
    private TextView tv_className, tv_dateTime, tv_attended;
    private View viewQR, qrPreview;
    private QRCodeReaderView qrCodeReaderView;
    private String classID, courseCode, uid, matric, dateTime, attended, className;
    StorageReference storageRef;
    DatabaseReference dataRef;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_join_class);

        tv_dateTime = findViewById(R.id.tv_classDateTime);
        tv_attended = findViewById(R.id.tv_students);
        tv_className = findViewById(R.id.tv_className);
        qrCodeReaderView = findViewById(R.id.qrdecoderview);
        classID = getIntent().getStringExtra("classID");
        courseCode = getIntent().getStringExtra("courseCode");
        user = auth.getInstance().getCurrentUser();
        uid = user.getUid();



        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
//        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
        qrCodeReaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qrCodeReaderView.startCamera();
            }
        });

        qrCodeReaderView.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {
            @Override
            public void onQRCodeRead(String text, PointF[] points) {

                Toast.makeText(StudJoinClass.this,text,Toast.LENGTH_SHORT).show();
                storageRef = FirebaseStorage.getInstance().getReference("/classes/"+courseCode+"/").child(text);
                String text2 = storageRef.getName();
                if(text.equals(text2)){
                    //get matric number of the current user
                    qrCodeReaderView.stopCamera();
                    DatabaseReference sDataRef = FirebaseDatabase.getInstance().getReference("/users/student/"+uid+"/");
                    sDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            matric = dataSnapshot.child("matric").getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    dataRef = FirebaseDatabase.getInstance().getReference("/classes/"+courseCode+"/"+classID+"/");
                    dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataRef.child("attend_list").child(matric).setValue(true);
                            tv_className.setText(dataSnapshot.child("className").getValue(String.class));
                            tv_dateTime.setText(dataSnapshot.child("date").getValue(String.class)+", "+
                                    dataSnapshot.child("startTime").getValue(String.class));

                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    dataRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            tv_attended.setText("Attended: "+dataSnapshot.child("attend_list").getChildrenCount());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    Toast.makeText(StudJoinClass.this,"Something Wrong", Toast.LENGTH_SHORT).show();
                }


                tv_className.setText(text);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }


}
