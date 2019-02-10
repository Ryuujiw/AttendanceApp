package com.example.ryuu.attendanceapp.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.R;
import com.example.ryuu.attendanceapp.objects.Classes;
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
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddClassActivity extends AppCompatDialogFragment {

    EditText class_title, class_date, class_time, class_venue;
    addClassActivityListener addClassListener;
    DatePickerDialog.OnDateSetListener mDateSetListener;
    TimePickerDialog.OnTimeSetListener mTimeSetListener;
    DatabaseReference mDataRef;
    StorageReference mStorageRef;
    String key;
    Bitmap bitmap;
    String courseCode;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle extras = getActivity().getIntent().getExtras();

        courseCode = extras.getString("courseCode");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_add_class, null);

        class_title = view.findViewById(R.id.edittext_title);
        class_date = view.findViewById(R.id.edittext_date);
        class_time = view.findViewById(R.id.et_time);
        class_venue = view.findViewById(R.id.et_venue);


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
                String venue = class_venue.getText().toString();
                addClassListener.applyText(title, date, time);

                Toast.makeText(getActivity(), "Class added", Toast.LENGTH_SHORT).show();
                mDataRef = FirebaseDatabase.getInstance().getReference("/classes/"+courseCode+"/");
                key = mDataRef.push().getKey();
                Classes classes = new Classes(key, title,date, time, venue);

                Map<String, Object> dataMap = new HashMap<String, Object>();
                dataMap.put(key, classes);
                mDataRef.updateChildren(dataMap);

                //generate bitmap from key
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(key, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    bitmap = barcodeEncoder.createBitmap(bitMatrix);
                }catch (WriterException e) {
                    e.printStackTrace();
                }
                //upload QR image into StorageDatabase
                mStorageRef = FirebaseStorage.getInstance().getReference("classes/"+courseCode+"/");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = mStorageRef.child(key).putBytes(data);


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
                DecimalFormat df = new DecimalFormat("00");
                String time = df.format(hour) +":"+ df.format(minute);
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

}