package de.grocery_scanner.helper.scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import de.grocery_scanner.MainActivity;
import de.grocery_scanner.R;
import de.grocery_scanner.api.VolleyCallback;
import de.grocery_scanner.api.EanDatabase;
import de.grocery_scanner.helper.insertean.InsertEan;
import de.grocery_scanner.persistence.dao.InventoryDAO;
import de.grocery_scanner.persistence.elements.Ean;
import de.grocery_scanner.viewmodel.EanViewModel;
import de.grocery_scanner.viewmodel.MainViewModel;

public class BarcodeScanner extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private EanViewModel eanViewModel;
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private TextView barcodeText;
    private TextView addedNumber;
    private LinearLayout bottomNav;
    private String barcodeData;

    private String barCode;
    private FloatingActionButton nextBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        eanViewModel = ViewModelProviders.of(this).get(EanViewModel.class);

        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
        surfaceView = findViewById(R.id.surface_view);
        barcodeText = findViewById(R.id.barcode_text);
        addedNumber = findViewById(R.id.addedNumber);
        bottomNav = findViewById(R.id.bottomNav);

        nextBtn = findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        initialiseDetectorsAndSources();

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
                    if (ActivityCompat.checkSelfPermission(BarcodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(BarcodeScanner.this, new
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
            Boolean activation = false;

            @Override
            public synchronized void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
                cameraSource.stop();
                activation = true;
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();


                if (barcodes.size() != 0) {

                    ExecutorService pool = Executors.newFixedThreadPool(3);
                    Future<String> future = pool.submit(new Callable<String>(){

                        @Override
                        public synchronized String call() throws Exception {
                            if(!activation) {

                                barcodeData = barcodes.valueAt(0).displayValue;

                                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP, 150);
                                activation = true;
                                return barcodeData;
                            }else{
                                return null;
                            }


                        }
                    });

                    if(!activation) {
                        try {

                            barCode = future.get();

                            int countEntries = eanViewModel.checkEan(barCode);

                            if (countEntries != 0) {
                                Ean ean = eanViewModel.getItemById(barCode);
                                String productName = ean.getName();
                                barcodeText.setText(productName);
                                mainViewModel.insertInventorybyGroupId(ean.getGroupId());
                            } else {
                                EanDatabase eanD = new EanDatabase(barCode, "http://opengtindb.org/", getApplicationContext());
                                eanD.getProduct(new VolleyCallback() {
                                    @Override
                                    public void onSuccessResponse(String result) {

                                        if (result != null) {
                                            barcodeText.setText(result);

                                            //insert product into database
                                         /*   Ean newEan = new Ean();
                                            newEan.setEanId(barCode);
                                            newEan.setName(result);
                                            eanViewModel.insert(newEan);
                                            mainViewModel.insertInventorybyEan(barCode);*/

                                            insertEan();
                                        } else {
                                            insertEan();
                                        }
                                    }

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        insertEan();
                                        Log.d("Error", "onErrorResponse: " + error);
                                    }
                                });

                            }

                            int increaseNumber = Integer.parseInt(addedNumber.getText().toString()) + 1;
                            addedNumber.setText("" + increaseNumber);

                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }


                    bottomNav.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.red));
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    activation = false;
                    bottomNav.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                }
            }
        });
    }

    private void insertEan(){
        Intent insertEanIntent = new Intent(getApplicationContext(), InsertEan.class);
        insertEanIntent.putExtra("ean", barCode);
        startActivity(insertEanIntent);
    }
}
