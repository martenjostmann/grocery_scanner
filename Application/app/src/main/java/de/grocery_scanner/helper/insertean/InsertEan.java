package de.grocery_scanner.helper.insertean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import de.grocery_scanner.R;
import de.grocery_scanner.helper.scanner.BarcodeScanner;
import de.grocery_scanner.inventory.InventoryFragment;
import de.grocery_scanner.persistence.dao.InventoryDAO;
import de.grocery_scanner.persistence.elements.ArticleGroup;
import de.grocery_scanner.persistence.elements.Ean;
import de.grocery_scanner.viewmodel.ArticleGroupViewModel;
import de.grocery_scanner.viewmodel.EanViewModel;
import de.grocery_scanner.viewmodel.MainViewModel;

public class InsertEan extends AppCompatActivity implements ArticleGroupAdapter.OnItemListener{

    private MainViewModel mainViewModel;
    private EanViewModel eanViewModel;
    private ArticleGroupViewModel articleGroupViewModel;
    private String ean;
    private FloatingActionButton addEan;
    private TextView addProductText;
    //private Dialog dialog;
    private ArticleGroupAdapter articleGroupAdapter;
    private MaterialAlertDialogBuilder dialog;
    private TextInputLayout editText;
    private TextInputEditText textInputEditText;
    private ArticleGroup selectedGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_ean);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        eanViewModel = ViewModelProviders.of(this).get(EanViewModel.class);
        articleGroupViewModel = ViewModelProviders.of(this).get(ArticleGroupViewModel.class);

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

        //Initialize Article Groups
        List<ArticleGroup> articleGroups = new ArrayList<ArticleGroup>();  //initialize inventory
        List<ArticleGroup> filterArticleGroups = new ArrayList<>();

        addProductText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new MaterialAlertDialogBuilder(InsertEan.this);

                View customAlertDialogView = LayoutInflater.from(InsertEan.this).inflate(R.layout.insert_ean_article_group, null, false);

                editText = customAlertDialogView.findViewById(R.id.textInputLayout);
                textInputEditText = customAlertDialogView.findViewById(R.id.textInputEditText);
                RecyclerView recyclerView = customAlertDialogView.findViewById(R.id.group_list);
                MaterialButton addGroup = customAlertDialogView.findViewById(R.id.add_group_button);

                dialog.setView(customAlertDialogView)
                        .setTitle("Artikelgruppe wählen")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addProductText.setText(selectedGroup.getName());
                            }
                        })
                        .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();


                articleGroupAdapter = new ArticleGroupAdapter(articleGroups, filterArticleGroups, InsertEan.this);

                recyclerView.setLayoutManager(new LinearLayoutManager(InsertEan.this));

                recyclerView.setAdapter(articleGroupAdapter);

                articleGroupViewModel.getAllArticleGroups().observe(InsertEan.this, new Observer<List<ArticleGroup>>() {
                    @Override
                    public void onChanged(List<ArticleGroup> articleGroups) {
                        articleGroupAdapter.setArticleGroups(articleGroups);
                        articleGroupAdapter.setFilteredArticleGroups(articleGroups);
                    }
                });

                textInputEditText.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        List<ArticleGroup> filterArticleGroups = articleGroupAdapter.getArticleGroups().stream().filter(x -> x.getName().toLowerCase()
                                .matches(".*"+ Pattern.compile(Pattern.quote(s.toString().toLowerCase()), Pattern.CASE_INSENSITIVE) +".*"))
                                .collect(Collectors.toList());
                        articleGroupAdapter.setFilteredArticleGroups(filterArticleGroups);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                addGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArticleGroup articleGroup = new ArticleGroup();
                        articleGroup.setName(editText.getEditText().getText().toString());

                        articleGroupViewModel.insert(articleGroup);
                    }
                });

            }

        });
    }

    private void insertProduct(){

        //Create new ean object
        Ean newEan = new Ean();
        newEan.setEanId(ean);
        newEan.setName(addProductText.getText().toString());

        //Insert newEan into database
        eanViewModel.insert(newEan);

        //Show info
        Toast toast = Toast.makeText(this, "Produkt hinzugefügt", Toast.LENGTH_LONG);
        toast.show();

        mainViewModel.insertInventorybyEan(ean);

        startActivity(new Intent(InsertEan.this, BarcodeScanner.class));
    }


    @Override
    public void onItemClick(ArticleGroup articleGroup) {
        Log.d("TAG", "onItemClick: CHANGED!!!!!");
        selectedGroup = articleGroup;
        articleGroupAdapter.notifyDataSetChanged();
        textInputEditText.clearFocus();
    }
}