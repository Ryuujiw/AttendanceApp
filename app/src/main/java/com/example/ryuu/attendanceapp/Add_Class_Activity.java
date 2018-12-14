package com.example.ryuu.attendanceapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;

public class Add_Class_Activity extends AppCompatDialogFragment {

    EditText class_title, class_date, class_time;
    addClassActivityListener addClassListener;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    TimePickerDialog.OnTimeSetListener mTimeSetListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_add__class_, null);

        class_title = view.findViewById(R.id.edittext_title);
        class_date = view.findViewById(R.id.edittext_date);
        class_time = view.findViewById(R.id.et_time);

        builder.setView(view).setTitle("Create class here :");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title = class_title.getText().toString();
                String date = class_date.getText().toString();
                String time = class_time.getText().toString();
                addClassListener.applyText(title, date, time);

            }

        });

        class_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentDate = Calendar.getInstance();
                //datepicker
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String date = year+"-"+month+"-"+day;
                class_date.setText(date);

            }
        };

        class_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTime = Calendar.getInstance();
                int selectedHour = currentTime.get(Calendar.HOUR_OF_DAY);
                int selectedMinute = currentTime.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener,
                        selectedHour, selectedMinute,true);
                timePickerDialog.show();
            }
        });


        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                String time = hour +":"+minute;
                class_time.setText(time);

            }
        };

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            addClassListener = (addClassActivityListener)context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement addClassActivityListener");
        }
    }

    public interface addClassActivityListener{
        void applyText(String title, String date, String time);
    }


    public EditText getClass_title() {
        return class_title;
    }

    public void setClass_title(EditText class_title) {
        this.class_title = class_title;
    }

    public EditText getClass_date() {
        return class_date;
    }

    public void setClass_date(EditText class_date) {
        this.class_date = class_date;
    }

    public EditText getClass_time() {
        return class_time;
    }

    public void setClass_time(EditText class_time) {
        this.class_time = class_time;
    }
}
