package de.grocery_scanner.helper.insertEan;

import androidx.appcompat.app.AppCompatActivity;

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
import de.grocery_scanner.helper.insertInventory.insertInventory;
import de.grocery_scanner.helper.scanner.barcodeScanner;
import de.grocery_scanner.persistence.elements.ean;
import de.grocery_scanner.persistence.instantiateDatabase;

public class insertEan extends AppCompatActivity {

    private String ean;
    private FloatingActionButton addEan;
    private EditText addProductText;

    private de.grocery_scanner.persistence.dao.eanDAO eanDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_ean);

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

        //create connection
        AppDatabase database = new instantiateDatabase().getDatabase(getApplicationContext());
        eanDAO = database.getEanDAO();

        //Create new ean object
        ean newEan = new ean();
        newEan.setEanId(ean);
        newEan.setName(addProductText.getText().toString());

        //Insert newEan into database
        eanDAO.insert(newEan);

        //Show info
        Toast toast = Toast.makeText(this, "Produkt hinzugefügt", Toast.LENGTH_LONG);
        toast.show();

        new insertInventory(database,ean);

        startActivity(new Intent(insertEan.this, barcodeScanner.class));
    }
}