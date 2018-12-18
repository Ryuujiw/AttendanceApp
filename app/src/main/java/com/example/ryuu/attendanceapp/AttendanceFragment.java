package com.example.ryuu.attendanceapp;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.objects.Class;
import com.example.ryuu.attendanceapp.objects.Classes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFragment extends Fragment {
    CardView cardView_status, cardView_classSummary;
    FloatingActionButton floatingActionButton;
    TextView tv_classStatus, tv_Hint, tv_qrurl ,tv_no_attendant;
    TextView tv_date, tv_noAttend, tv_location, tv_startTime, tv_endTime, tv_className;
    String date, location, startTime,endTime, classname;
    ImageView iv_QRcode;
    String loginMode;//Mode
    String getResult;
    DatabaseReference mDataRef;
    StorageReference mStorageRef;
    boolean status;
    ZXingScannerView zXingScannerView;
    String previousClassName, previousCLassID;
    Classes classes;
    String reference;


    public AttendanceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        // GET LOGIN MODE
        loginMode = getActivity().getIntent().getStringExtra("LoginMode");
        previousCLassID = getActivity().getIntent().getStringExtra("classID");
        floatingActionButton = view.findViewById(R.id.fab_QRScanner);
        tv_qrurl = view.findViewById(R.id.tv_qrURL);
        iv_QRcode = view.findViewById(R.id.iv_generated_qrcode);
        tv_classStatus = view.findViewById(R.id.tv_class_status);
        tv_no_attendant = view.findViewById(R.id.tv_no_of_attendant);
        cardView_classSummary = view.findViewById(R.id.cv_classSummary);
        cardView_classSummary.setVisibility(View.INVISIBLE);
        cardView_status = view.findViewById(R.id.cv_attendance_status);
        iv_QRcode.setVisibility(View.INVISIBLE);
        tv_date = view.findViewById(R.id.tv_date);
        tv_noAttend = view.findViewById(R.id.tv_noAttend);
        tv_location = view.findViewById(R.id.tv_location);
        tv_startTime = view.findViewById(R.id.tv_startTime);
        tv_endTime = view.findViewById(R.id.tv_endTime);
        tv_className = view.findViewById(R.id.tv_className);
        tv_Hint = view.findViewById(R.id.tv_hint);
        Intent intent = getActivity().getIntent();
        previousClassName = intent.getStringExtra("className");
        Toast.makeText(getActivity(),loginMode, Toast.LENGTH_SHORT);
        reference = "/classes/networkw1/"+previousCLassID+"/";
        if (loginMode.equals("teacher")) {
            floatingActionButton.setVisibility(View.INVISIBLE);
            mDataRef = FirebaseDatabase.getInstance().getReference(reference);
            mDataRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot classSnapshot) {
                    if (classSnapshot.exists()) {
                        classes = classSnapshot.getValue(Classes.class);
                        String qr = classes.getQrUrl();
                        tv_qrurl.setText(qr);
                        // retrieve the classes status
                        if (classes.isStatus() == true) {
                            cardView_status.setBackgroundColor(Color.parseColor("#FF99CC00"));
                            tv_classStatus.setText("Ongoing");
                            tv_no_attendant.setText(String.valueOf(classSnapshot.child("attend_list").getChildrenCount()));
                        } else if (classes.isStatus()  == false) {
                            cardView_status.setBackgroundColor(Color.parseColor("#FFCC0000"));//Red
                            tv_classStatus.setText("Press Here to Start/End Class");
                        }
                        // retrieve the QR bitmap from FirebaseStorage
                        mStorageRef = FirebaseStorage.getInstance().getReference();
                        StorageReference islandRef = mStorageRef.child("/classes/network/qrImage/");
                        final long ONE_MEGABYTE = 1024 * 1024;
                        islandRef.child(previousCLassID).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                iv_QRcode.setImageBitmap(bmp);
                                iv_QRcode.setVisibility(View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {

                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "the class has been deleted", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            cardView_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                date = dataSnapshot.child("date").getValue(String.class);
                                location = dataSnapshot.child("location").getValue(String.class);
                                startTime = dataSnapshot.child("startTime").getValue(String.class);
                                classname = dataSnapshot.child("className").getValue(String.class);


                                status = dataSnapshot.child("status").getValue(boolean.class);
                                if (status == false) {
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setTitle("Start Class");
                                    alertDialogBuilder.setMessage("Are you sure you want to START the class? This class will be visible to students");
                                    alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            mDataRef.child("status").setValue(false);
                                            status = false;
                                        }
                                    });
                                    alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {//pressed yes to end
                                            cardView_status.setBackgroundColor(Color.parseColor("#FF99CC00"));//green color
                                            tv_classStatus.setText("Ongoing");
                                            cardView_classSummary.setVisibility(View.INVISIBLE);
                                            mDataRef.child("status").setValue(true);
                                            mDataRef.child("open").setValue(true);
                                            status = true;
                                        }
                                    }).show();


                                } else if (status == true) {
                                    AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(getActivity());
                                    alertDialogBuilder2.setTitle("End Class");
                                    alertDialogBuilder2.setMessage("Are you sure you want to END the Class?");
                                    alertDialogBuilder2.setPositiveButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            mDataRef.child("status").setValue(true);
                                            status = true;
                                        }
                                    });
                                    alertDialogBuilder2.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {//pressed yes to start class
                                            cardView_status.setBackgroundColor(Color.parseColor("#FFCC0000"));//Red
                                            tv_classStatus.setText("Ended");
                                            iv_QRcode.setVisibility(View.INVISIBLE);
                                            tv_date.setText(date);
                                            tv_location.setText(location);
                                            Calendar calendar = Calendar.getInstance();
                                            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                                            endTime = format.format(calendar.getTime());
                                            tv_startTime.setText(startTime);
                                            tv_endTime.setText(endTime);
                                            tv_className.setText(classname);
                                            mDataRef.child("endTime").setValue(endTime);
                                            cardView_classSummary.setVisibility(View.VISIBLE);
                                            Toast.makeText(getContext(), "Class summary is generated", Toast.LENGTH_SHORT).show();
                                            mDataRef.child("status").setValue(false);
                                            status = false;
                                        }
                                    }).show();

                                }
                            } else {
                                Toast.makeText(getContext(), "no datasnapshot", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
        }else if (loginMode.equals("student")) {
            mDataRef = FirebaseDatabase.getInstance().getReference(reference);
            mDataRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean status =dataSnapshot.child("status").getValue(boolean.class);
                    boolean open = dataSnapshot.child("open").getValue(boolean.class);
                    if (status == true) {
                        cardView_status.setBackgroundColor(Color.parseColor("#FF99CC00"));
                        tv_classStatus.setText("Ongoing");
                        tv_no_attendant.setText(String.valueOf(dataSnapshot.child("attend_list").getChildrenCount()));
                        floatingActionButton.setVisibility(View.VISIBLE);
                        cardView_classSummary.setVisibility(View.INVISIBLE);
                        tv_Hint.setText("Please Scan to take Attendance");


                    } else if (status == false) {
                        cardView_status.setBackgroundColor(Color.parseColor("#FFCC0000"));//Red
                        tv_classStatus.setText("Not Open");
                        if(open == true && status == false){
                            floatingActionButton.setVisibility(View.INVISIBLE);
                            tv_Hint.setText("Class has ended, No more QR code");
                        }
                        tv_startTime.setText(dataSnapshot.child("startTime").getValue(String.class));
                        tv_endTime.setText(dataSnapshot.child("endTime").getValue(String.class));
                        tv_className.setText(dataSnapshot.child("className").getValue(String.class));
                        tv_date.setText(dataSnapshot.child("date").getValue(String.class));
                        tv_location.setText(dataSnapshot.child("location").getValue(String.class));
                        cardView_classSummary.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    zXingScannerView = new ZXingScannerView(view.getContext());
                    getActivity().setContentView(zXingScannerView);
                    zXingScannerView.setResultHandler(new ZXingScannerView.ResultHandler() {
                        @Override
                        public void handleResult(Result rawResult) {
                            Toast.makeText(getContext(),rawResult.getText(),Toast.LENGTH_SHORT).show();

                            getResult = rawResult.getText();
                            mDataRef = FirebaseDatabase.getInstance().getReference(reference);
                            mDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String qrCode = dataSnapshot.child("qrUrl").getValue(String.class);
                                    if(getResult.equals(qrCode)){
                                        mDataRef.child("attend_list").child("A160979").setValue(true);
                                        zXingScannerView.stopCamera();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });
                    zXingScannerView.startCamera();
                }
            });
        }


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getContext(), "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), result.getContents(), Toast.LENGTH_LONG).show();

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}