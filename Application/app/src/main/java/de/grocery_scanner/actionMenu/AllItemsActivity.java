package de.grocery_scanner.actionMenu;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.grocery_scanner.MainActivity;
import de.grocery_scanner.R;
import de.grocery_scanner.persistence.dao.InventoryDAO;
import de.grocery_scanner.viewmodel.EanViewModel;

import de.grocery_scanner.persistence.dao.EanDAO.ItemsWithCount;

public class AllItemsActivity extends AppCompatActivity implements AllItemsAdapter.OnItemListener {

    private EanViewModel eanViewModel;
    private RecyclerView allItemsList;
    private AllItemsAdapter allItemsAdapter;
    private String lastSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        AppBarLayout appBar = findViewById(R.id.app_bar);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

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

        MenuItem searchItem = menu.findItem(R.id.action_search);
        MaterialSearchView searchView = findViewById(R.id.search_view);
        searchView.setMenuItem(searchItem);

        // Define elements for custom design
        ImageButton mEmptyBtn = (ImageButton) findViewById(com.miguelcatalan.materialsearchview.R.id.action_empty_btn);
        EditText mSearchSrcTextView = (EditText) findViewById(com.miguelcatalan.materialsearchview.R.id.searchTextView);
        ImageButton mBackBtn = (ImageButton) findViewById(com.miguelcatalan.materialsearchview.R.id.action_up_btn);

        // Listener when search empty button was pressed
        mEmptyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // The text view text and the last search string should be cleared
                mSearchSrcTextView.setText(null);
                lastSearch = "";
                search(lastSearch);
            }
        });

        // Define OnQueryTextListener explicitly, so that it can be turned off and on
        MaterialSearchView.OnQueryTextListener mOnQueryListener = new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String name) {
                search(lastSearch);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String name) {
                lastSearch = name;
                search(name);
                return false;
            }
        };

        // Listener when back button was pressed
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // reset search
                searchView.setOnQueryTextListener(null);
                lastSearch = "";
                search(lastSearch);
                searchView.closeSearch();
                searchView.setOnQueryTextListener(mOnQueryListener);

            }
        });

        // Listener when search button on the keyboard was pressed
        mSearchSrcTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //perform search and close search afterwards
                    searchView.setOnQueryTextListener(null);
                    search(lastSearch);
                    searchView.closeSearch();
                    searchView.setOnQueryTextListener(mOnQueryListener);

                    return true;
                }

                return false;
            }
        });

        searchView.setOnQueryTextListener(mOnQueryListener);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Set lass search when the search was opened again
                searchView.setQuery(lastSearch, false);
            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        return true;
    }



    public void search(String name) {
        eanViewModel.getItemsWithCountSearch(name).observe(AllItemsActivity.this, new Observer<List<ItemsWithCount>>() {
            @Override
            public void onChanged(List<ItemsWithCount> itemsWithCounts) {
                allItemsAdapter.setAllItems(itemsWithCounts);
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }
}