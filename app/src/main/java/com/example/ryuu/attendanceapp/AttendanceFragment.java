package com.example.ryuu.attendanceapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFragment extends Fragment {
    private String loginMode;
    CardView cardView_status, cardView_classSummary;
    FloatingActionButton floatingActionButton;
    TextView tv_classStatus;
    ImageView iv_QRcode;

    public AttendanceFragment() {
        // Required empty public constructor
    }

    int colorMode =0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        // GET LOGIN MODE
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            loginMode = bundle.getString("LOGIN_MODE", "");
        }

        floatingActionButton = view.findViewById(R.id.fab_QRScanner);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loginMode.equals("teacher")){
                    Intent intent = new Intent(getActivity(), ClassList_Teacher_Activity.class);
                    startActivity(intent);
                } else if(loginMode.equals("student")){
                    Intent intent = new Intent(getActivity(),QRScanner.class);
                    startActivity(intent);
                }
            }
        });
        iv_QRcode = view.findViewById(R.id.iv_generated_qrcode);
        iv_QRcode.setVisibility(View.INVISIBLE);
        tv_classStatus = view.findViewById(R.id.tv_class_status);
        cardView_classSummary = view.findViewById(R.id.cv_classSummary);
        cardView_classSummary.setVisibility(View.INVISIBLE);
        cardView_status = view.findViewById(R.id.cv_attendance_status);


        cardView_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (colorMode){
                    case 0:
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setTitle("Start Class");
                        alertDialogBuilder.setMessage("Are you sure you want to START the class?");
                        alertDialogBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                colorMode=0;
                            }
                        });
                        alertDialogBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cardView_status.setBackgroundColor(Color.parseColor("#FF99CC00"));//green color
                                tv_classStatus.setText("Ongoing");
                                iv_QRcode.setVisibility(View.VISIBLE);
                                cardView_classSummary.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(),"QR Code is generated", Toast.LENGTH_SHORT).show();
                                colorMode=1;
                            }
                        }).show();


                        break;
                    case 1:
                        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(getActivity());
                        alertDialogBuilder2.setTitle("End Class");
                        alertDialogBuilder2.setMessage("Are you sure you want to END the Class?");
                        alertDialogBuilder2.setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                colorMode=1;
                            }
                        });
                        alertDialogBuilder2.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                cardView_status.setBackgroundColor(Color.parseColor("#FFCC0000"));//Red
                                tv_classStatus.setText("Ended");
                                iv_QRcode.setVisibility(View.INVISIBLE);
                                cardView_classSummary.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(),"Class summary is generated", Toast.LENGTH_SHORT).show();
                                colorMode=0;
                            }
                        }).show();
                        break;
                }
            }
        });
        return view;
    }

}
