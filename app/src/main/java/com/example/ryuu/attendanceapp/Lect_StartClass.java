package com.example.ryuu.attendanceapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Lect_StartClass extends AppCompatActivity {
    private FirebaseUser User;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private TextView tv_date, tv_className, tv_startTime, tv_endTime, tv_attendance;
    private Button btn_generateQR, btn_deactiveQR;
    private String currentEmail, uid, reference, previousCLassID, courseCode, previousClassName;
    private String className, date, startTime, endTime,attendNumber;
    private boolean status;
    private Intent emailIntent;

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

        tv_className = findViewById(R.id.tv_class_name);
        tv_date = findViewById(R.id.tv_class_date);
        tv_startTime = findViewById(R.id.tv_start_time);
        tv_endTime = findViewById(R.id.tv_end_time);
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
//                String qr = classSnapshot.child("qrUrl").getValue(String.class);
                previousClassName = classSnapshot.child("className").getValue(String.class);
                // retrieve the classes status
                if ( classSnapshot.child("status").getValue(boolean.class) == true) {
                    date = classSnapshot.child("date").getValue(String.class);
                    startTime = classSnapshot.child("startTime").getValue(String.class);
                    className = classSnapshot.child("className").getValue(String.class);
                    tv_className.setText(className);
                    tv_date.setText(date);
                    tv_startTime.setText(startTime);
                    attendNumber = String.valueOf(classSnapshot.child("attend_list").getChildrenCount());
                    tv_attendance.setText(attendNumber);
                    btn_generateQR.setText("End Class");

                } else if (classSnapshot.child("status").getValue(boolean.class)  == false) {
                    date = classSnapshot.child("date").getValue(String.class);
                    startTime = classSnapshot.child("startTime").getValue(String.class);
                    className = classSnapshot.child("className").getValue(String.class);
                    tv_className.setText(className);
                    tv_date.setText(date);
                    tv_startTime.setText(startTime);
                    tv_attendance.setText("Class Ended/Press to view report");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        btn_generateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot){

                        status = dataSnapshot.child("status").getValue(boolean.class);
                        if (status == false) {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Lect_StartClass.this);
                            alertDialog.setTitle("Start Class");
                            alertDialog.setMessage("Generate QR and start class? The QR will be send to "+currentEmail);
                            alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {//pressed no
                                    databaseReference.child("status").setValue(false);
                                }
                            });
                            alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {//pressed yes to start
                                    databaseReference.child("status").setValue(true);
                                    databaseReference.child("open").setValue(true);
                                    btn_generateQR.setText("End Class");

                                    //send QR Code link to lecturer's email
                                    emailIntent = new Intent(Intent.ACTION_SEND);
                                    emailIntent.setType("img/png");
                                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {currentEmail});
                                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "QR Image for "+previousClassName);

                                    StorageReference mStoreRef = FirebaseStorage.getInstance().getReference("/classes/"+courseCode+"/").child(previousCLassID);
                                    mStoreRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String URL = uri.toString();
                                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Please display this QR Image for student to scan "+URL);
                                            startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
                                        }
                                    });

                                }
                            }).show();
                        }else{//class is running, status is true and pressed to stop
                            AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(Lect_StartClass.this);
                            alertDialogBuilder2.setTitle("End Class");
                            alertDialogBuilder2.setMessage("END the Class?");
                            alertDialogBuilder2.setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    databaseReference.child("status").setValue(true);
                                    status = true;
                                }
                            });
                            alertDialogBuilder2.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {//pressed yes to end class
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                                    endTime = format.format(calendar.getTime());
                                    tv_startTime.setText(startTime);
                                    tv_endTime.setText(endTime);
                                    tv_className.setText(className);
                                    databaseReference.child("endTime").setValue(endTime);
                                    databaseReference.child("status").setValue(false);
                                    status = false;
                                    btn_generateQR.setText("Report Summary");
                                }
                            }).show();
                        }
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
                builder.setTitle("Delete Class");
                builder.setMessage("Stop the scan?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference deleteRef = storageRef.child("/classes/"+courseCode+"/"+previousCLassID);
                        deleteRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Lect_StartClass.this, "QR stopped", Toast.LENGTH_SHORT).show();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Lect_StartClass.this, "Already Stopped", Toast.LENGTH_SHORT).show();
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
    }
}
