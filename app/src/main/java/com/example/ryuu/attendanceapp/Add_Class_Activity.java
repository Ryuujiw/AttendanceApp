package com.example.ryuu.attendanceapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;

public class Add_Class_Activity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    EditText class_title, class_date, class_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__class_);

        class_title = (EditText)findViewById(R.id.edittext_title);
        class_date = (EditText)findViewById(R.id.edittext_date);
        class_time = (EditText)findViewById(R.id.edittext_time);

        class_date.setOnClickListener(this);
        class_time.setOnClickListener(this);


    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        String date_text = year+"-"+month+"-"+day;
        class_date.setText(date_text);
    }

    @Override
    public void onClick(View view) {

        Calendar currentDate = Calendar.getInstance();
        //datepicker
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(Add_Class_Activity.this, this, year, month, day);
        datePickerDialog.show();

        //timepicker
        int selected_hour = currentDate.get(Calendar.HOUR_OF_DAY);
        int selected_minute = currentDate.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(Add_Class_Activity.this, this, selected_hour, selected_minute, true);
        timePickerDialog.show();

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        String time_text = hour +":"+minute;
        class_date.setText(time_text);
    }
}
