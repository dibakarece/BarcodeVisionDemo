package com.barcodescangvision;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class TestClass extends AppCompatActivity {

    private static final String TAG = com.barcodescangvision.TestClass.class.getSimpleName();
    private Context context;
    private Handler handler = new Handler();
    private SurfaceView preview;
    private CameraSource mCameraSource;
    private CustomRequestPreview requestPreview;
    private BarcodeDetector barcodeDetector;

    private long nextDelayTime = 5 * 1000;
    private boolean isAcceptValue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        context = TestClass.this;
        initializeViews();
        startScanning();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(task);
    }


    private void initializeViews() {
        preview = (SurfaceView) findViewById(R.id.preview);
        requestPreview = (CustomRequestPreview) findViewById(R.id.requestPreview);
        startScanning();
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            isAcceptValue = true;
        }
    };

    private void startScanning() {
        barcodeDetector = new BarcodeDetector.Builder(context).build();
        mCameraSource = new CameraSource.Builder(context, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(1)
                .setRequestedPreviewSize(400, 360)
                .build();

        preview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCameraSource.start(preview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCameraSource.stop();
            }
        });
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> items = detections.getDetectedItems();
                if (items != null && items.size() >= 1) {
                    final String code = items.valueAt(0).displayValue;
                    if (!isAcceptValue)
                        return;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TestClass.this, code, Toast.LENGTH_SHORT).show();
                            isAcceptValue = false;
                            handler.postDelayed(task, nextDelayTime);
                        }
                    });
                }
            }
        });
    }
}
