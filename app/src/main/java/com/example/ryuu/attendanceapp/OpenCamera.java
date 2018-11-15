package com.example.ryuu.attendanceapp.adapter;

import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class OpenCamera {
    SurfaceView cameraPreview;
    TextView txtScanStatus;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    int RequestCameraPermissionID = 1001;
}
