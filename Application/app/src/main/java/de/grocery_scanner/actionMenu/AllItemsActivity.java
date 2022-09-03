package de.grocery_scanner.actionMenu;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import de.grocery_scanner.MainActivity;
import de.grocery_scanner.R;
import de.grocery_scanner.persistence.dao.InventoryDAO;
import de.grocery_scanner.viewmodel.EanViewModel;

import de.grocery_scanner.persistence.dao.EanDAO.ItemsWithCount;

public class AllItemsActivity extends AppCompatActivity implements AllItemsAdapter.OnItemListener{

    private EanViewModel eanViewModel;
    private RecyclerView allItemsList;
    private AllItemsAdapter allItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllItemsActivity.this, MainActivity.class));
            }
        });

        afterCreate();
    }

    private void afterCreate() {
        eanViewModel =  ViewModelProviders.of(this).get(EanViewModel.class);

        List<ItemsWithCount> itemsWithCount = new ArrayList<ItemsWithCount>();

        allItemsAdapter = new AllItemsAdapter(itemsWithCount, this);

        allItemsList = findViewById(R.id.allItemsList);
        allItemsList.setLayoutManager(new LinearLayoutManager(this));

        // Set custom adapter
        allItemsList.setAdapter(allItemsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        allItemsList.addItemDecoration(dividerItemDecoration);

        // Get Inventory Items out of the database and watch changes
        eanViewModel.getItemsWithCount().observe(this, new Observer<List<ItemsWithCount>>() {
            @Override
            public void onChanged(List<ItemsWithCount> itemsWithCount) {
                allItemsAdapter.setAllItems(itemsWithCount);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_all_items, menu);
        return true;
    }

    @Override
    public void onItemClick(int position) {

    }
}