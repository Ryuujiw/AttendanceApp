package com.example.ryuu.attendanceapp.activities.klass;

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
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ryuu.attendanceapp.R;
import com.example.ryuu.attendanceapp.barcodeEncoder.BarcodeEncoder;
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

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddClassActivity extends AppCompatDialogFragment {

    protected EditText et_className, et_classDate;
    protected Spinner spinner_classType;
    protected TextInputLayout til_classType, til_className, til_classDate;
    protected addClassActivityListener addClassListener;
    protected DatePickerDialog.OnDateSetListener mDateSetListener;

    protected DatabaseReference mDataRef;
    protected StorageReference mStorageRef;
    protected String key;
    protected Bitmap bitmap;
    protected String courseCode;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        Bundle extras = getActivity().getIntent().getExtras();

        courseCode = extras.getString("courseCode");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_add_class, null);

        //declare variables
        this.spinner_classType = view.findViewById(R.id.spinner_classType);
        this.et_className = view.findViewById(R.id.et_className);
        this.et_classDate = view.findViewById(R.id.et_classDate);
        this.til_classType = view.findViewById(R.id.til_classType);
        this.til_className = view.findViewById(R.id.til_className);
        this.til_classDate = view.findViewById(R.id.til_classDate);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getContext(), R.array.class_type, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_classType.setAdapter(arrayAdapter);
        spinner_classType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

                String classType = spinner_classType.getSelectedItem().toString();
                String className = et_className.getText().toString();
                String classDate = et_classDate.getText().toString();
                addClassListener.applyText(className, classDate, classType);

                Toast.makeText(getActivity(), "Class added", Toast.LENGTH_SHORT).show();
                mDataRef = FirebaseDatabase.getInstance().getReference("/classes/"+courseCode+"/");
                key = mDataRef.push().getKey();
                Classes classes = new Classes(key, className, classDate, null, classType);

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

        et_classDate.setOnClickListener(new View.OnClickListener() {
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

                String date = day+"/"+month+"/"+year;
                et_classDate.setText(date);

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
        void applyText(String className, String classDate, String classType);
    }

}