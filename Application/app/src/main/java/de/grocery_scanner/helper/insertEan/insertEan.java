package de.grocery_scanner.helper.insertEan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.grocery_scanner.AppDatabase;
import de.grocery_scanner.MainActivity;
import de.grocery_scanner.R;
import de.grocery_scanner.helper.scanner.barcodeScanner;
import de.grocery_scanner.persistence.elements.ean;
import de.grocery_scanner.persistence.instantiateDatabase;
import de.grocery_scanner.viewModel.EanViewModel;
import de.grocery_scanner.viewModel.MainViewModel;

public class insertEan extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private EanViewModel eanViewModel;
    private String ean;
    private FloatingActionButton addEan;
    private EditText addProductText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_ean);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        eanViewModel = ViewModelProviders.of(this).get(EanViewModel.class);

        //get ean from barcodeScanner
        ean = getIntent().getStringExtra("ean");
        Log.d("TAG", "onCreate: " + ean);

        //set eanCode
        TextView eanCodeText = findViewById(R.id.eanCode);
        eanCodeText.setText(ean);

        //Name
        addProductText = findViewById(R.id.addProductText);

        //Insert Button
        addEan = findViewById(R.id.addProductButton);

        addEan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                insertProduct();
            }
        });
    }

    private void insertProduct(){

        //Create new ean object
        ean newEan = new ean();
        newEan.setEanId(ean);
        newEan.setName(addProductText.getText().toString());

        //Insert newEan into database
        eanViewModel.insert(newEan);

        //Show info
        Toast toast = Toast.makeText(this, "Produkt hinzugefügt", Toast.LENGTH_LONG);
        toast.show();

        mainViewModel.insertInventorybyEan(ean);

        startActivity(new Intent(insertEan.this, barcodeScanner.class));
    }
}