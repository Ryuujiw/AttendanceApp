package com.example.ryuu.attendanceapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.barcodeEncoder.BarcodeEncoder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class LectStartClass extends AppCompatActivity {
    private FirebaseUser User;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private Switch sw_className_onOff;
    private TextView tv_date, tv_startTime, tv_endTime, tv_attendance, tv_status;
    private Button btn_generateQR, btn_deactiveQR;
    private String currentEmail, uid, reference, previousCLassID, courseCode, previousClassName;
    private String className, date, startTime, endTime,attendNumber, QR_URL, student_list;
    private Intent emailIntent;
    private Bitmap bitmap;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lect__start_class);

        Toolbar toolbar = findViewById(R.id.tb_class);
        setSupportActionBar(toolbar);

        //get user matric from firebase
        firebaseAuth = firebaseAuth.getInstance();
        //get current user logged in
        User = firebaseAuth.getCurrentUser();
        currentEmail = User.getEmail();
        uid = User.getUid();

        sw_className_onOff = findViewById(R.id.sw_class_name);
        tv_date = findViewById(R.id.tv_class_date);
        tv_startTime = findViewById(R.id.tv_start_time);
        tv_endTime = findViewById(R.id.tv_end_time);
        tv_status = findViewById(R.id.tv_class_status);
        tv_attendance = findViewById(R.id.tv_attendance);

        btn_generateQR = findViewById(R.id.btn_generate_QR);
        btn_deactiveQR = findViewById(R.id.btn_deactivate_QR);

        previousCLassID = getIntent().getStringExtra("classID");
        courseCode = getIntent().getStringExtra("courseCode");

        reference = "/classes/"+courseCode+"/"+previousCLassID+"/";
        databaseReference = FirebaseDatabase.getInstance().getReference(reference);
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot classSnapshot) {
                QR_URL = classSnapshot.child("qrUrl").getValue(String.class);
                // retrieve the classes status
                previousClassName = classSnapshot.child("className").getValue(String.class);
                attendNumber = String.valueOf(classSnapshot.child("attend_list").getChildrenCount());
                date = classSnapshot.child("date").getValue(String.class);
                startTime = classSnapshot.child("startTime").getValue(String.class);
                className = classSnapshot.child("className").getValue(String.class);
                // apply into the textviews
                sw_className_onOff.setText(className);
                tv_date.setText(date);
                tv_startTime.setText(startTime);
                if ( classSnapshot.child("status").getValue(boolean.class) == true) {
                    sw_className_onOff.setChecked(true);
                    tv_attendance.setText(attendNumber);
                    tv_status.setText("Ongoing");
                    btn_generateQR.setEnabled(true);
                    btn_deactiveQR.setEnabled(true);

                } else if (classSnapshot.child("status").getValue(boolean.class)  == false) {
                    sw_className_onOff.setChecked(false);
                    if(attendNumber.equals("0")){
                        tv_status.setText("Created");
                        btn_generateQR.setEnabled(false);
                        btn_deactiveQR.setEnabled(false);
                    }else{
                        tv_status.setText("Ended");
                        btn_generateQR.setEnabled(true);
                        btn_deactiveQR.setEnabled(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}});

        sw_className_onOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((Switch) v).isChecked();
                if(on==false){
                    AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(LectStartClass.this);
                    alertDialogBuilder2.setTitle("End Class");
                    alertDialogBuilder2.setMessage("END the Class?");
                    alertDialogBuilder2.setCancelable(false);
                    alertDialogBuilder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {//pressed yes to end class
                            sw_className_onOff.setChecked(false);
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                            endTime = format.format(calendar.getTime());
                            tv_startTime.setText(startTime);
                            tv_endTime.setText(endTime);
                            databaseReference.child("endTime").setValue(endTime);
                            databaseReference.child("status").setValue(false);
                        }
                    });
                    alertDialogBuilder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            databaseReference.child("status").setValue(true);
                            sw_className_onOff.setChecked(true);
                        }
                    });
                    Dialog dialog = alertDialogBuilder2.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }else if(on==true){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LectStartClass.this);
                    alertDialog.setTitle("Start Class");
                    alertDialog.setMessage("Generate QR and start class? The QR will be send to "+currentEmail);
                    alertDialog.setCancelable(true);
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {//pressed yes to start
                            sw_className_onOff.setChecked(true);
                            databaseReference.child("status").setValue(true);
                            databaseReference.child("open").setValue(true);

                            //send QR Code link to lecturer's email
                            emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setType("img/png");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {currentEmail});
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "QR Image for "+previousClassName);

                            StorageReference mStoreRef = FirebaseStorage.getInstance().getReference("/classes/"+courseCode+"/").child(QR_URL);
                            mStoreRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String URL = uri.toString();
                                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Please display this QR Image for student to scan "+URL);
                                    startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
                                }

                            });
                        }
                    });
                    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {//pressed no
                            databaseReference.child("status").setValue(false);
                            sw_className_onOff.setChecked(false);
                        }
                    });
                    Dialog dialog = alertDialog.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
            }
        });
                btn_generateQR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                                //delete the old QR Code on realtime database
                                String newkey = getrandomString(10);
                                databaseReference.child("qrUrl").setValue(newkey);
                                StorageReference mStoreRef = FirebaseStorage.getInstance().getReference("/classes/"+courseCode+"/");
                                //delete the old QR code image
                                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                                StorageReference deleteRef = storageRef.child("/classes/"+courseCode+"/"+QR_URL);
                                deleteRef.delete();
                                //generate bitmap from new key
                                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                try {
                                    BitMatrix bitMatrix = multiFormatWriter.encode(newkey, BarcodeFormat.QR_CODE,200,200);
                                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                    bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                }catch (WriterException e) {
                                    e.printStackTrace();
                                }
                                //upload QR image into StorageDatabase
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] data = baos.toByteArray();
                                mStoreRef.child(newkey).putBytes(data);

                                Toast.makeText(LectStartClass.this,"New QR Code is Generated",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
        btn_deactiveQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Remove QR Code");
                builder.setMessage("Stop the scan?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference deleteRef = storageRef.child("/classes/"+courseCode+"/"+QR_URL);
                        deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LectStartClass.this, "QR stopped", Toast.LENGTH_SHORT).show();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LectStartClass.this, "Already Stopped", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        tv_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Student List");
//                student_list="";
                databaseReference.child("attend_list").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        student_list="";
                        for(DataSnapshot data:dataSnapshot.getChildren()){
                            student_list += data.getKey()+"\n";
                        }
                        builder.setMessage(student_list);
                        builder.setNeutralButton("OK",null);
                        builder.show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    public String getrandomString(int size){
        final String data = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(data.charAt(RANDOM.nextInt(data.length())));
        }

        return sb.toString();
    }

}
