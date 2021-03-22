package de.grocery_scanner.helper.scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.grocery_scanner.AppDatabase;
import de.grocery_scanner.R;
import de.grocery_scanner.api.VolleyCallback;
import de.grocery_scanner.api.eanDatabase;
import de.grocery_scanner.helper.insertEan.insertEan;
import de.grocery_scanner.helper.scraper.webScraper;
import de.grocery_scanner.persistence.elements.ean;
import de.grocery_scanner.persistence.instantiateDatabase;

public class barcodeScanner extends AppCompatActivity {

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private String barcodeData;

    private String barCode;
    private de.grocery_scanner.persistence.dao.eanDAO eanDAO;
    private String ApiResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);

        initialiseDetectorsAndSources();

//        try {
//            getCodeData();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    private void getCodeData() throws InterruptedException {

        webScraper webScraper = new webScraper("https://en.wikipedia.org/");
        Thread thread = new Thread(webScraper);
        thread.start();
        thread.join();

    }


    private void initialiseDetectorsAndSources() {

        //Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(barcodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(barcodeScanner.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();


                if (barcodes.size() != 0) {
                    ExecutorService pool = Executors.newFixedThreadPool(3);
                    Future<String> future = pool.submit(new Callable<String>(){

                        @Override
                        public String call() throws Exception {
                            if (barcodes.valueAt(0).email != null) {
                                barcodeText.removeCallbacks(null);
                                barcodeData = barcodes.valueAt(0).email.address;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            } else {
                                barcodeData = barcodes.valueAt(0).displayValue;
                                barcodeText.setText(barcodeData);
                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                            }
                            return barcodeData;
                        }
                    });

                    try {

                        barCode = future.get();

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                cameraSource.release();
                            }
                        });

                        AppDatabase database = new instantiateDatabase().getDatabase(getApplicationContext());
                        eanDAO = database.getEanDAO();

                        int countEntries = eanDAO.checkEan(barCode);

                        if(countEntries != 0){

                        }else{
                            eanDatabase eanD = new eanDatabase(barCode,"http://opengtindb.org/", getApplicationContext());
                            eanD.getProduct(new VolleyCallback() {
                                @Override
                                public void onSuccessResponse(String result) {
                                    Log.d("TAG", "onSuccessResponse: " + result);

                                    if(result != null) {
                                        ean newEan = new ean();
                                        newEan.setEanId(barCode);
                                        newEan.setName(result);
                                        eanDAO.insert(newEan);
                                    }else{
                                        Intent insertEanIntent = new Intent(getApplicationContext(), insertEan.class);
                                        insertEanIntent.putExtra("ean", barCode);
                                        startActivity(insertEanIntent);
                                    }
                                }
                            });

                            Log.d("TAG", "test: " + ApiResult);

                        }

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


}