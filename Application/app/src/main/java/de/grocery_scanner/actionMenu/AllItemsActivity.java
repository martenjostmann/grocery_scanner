package de.grocery_scanner.actionMenu;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import de.grocery_scanner.persistence.elements.Inventory;
import de.grocery_scanner.viewmodel.ArticleGroupViewModel;

import de.grocery_scanner.persistence.dao.ArticleGroupDAO.ItemsWithCount;
import de.grocery_scanner.viewmodel.MainViewModel;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class AllItemsActivity extends AppCompatActivity implements AllItemsAdapter.OnItemListener {

    private ArticleGroupViewModel articleGroupViewModel;
    private MainViewModel mainViewModel;
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
        // Add View Models
        articleGroupViewModel = ViewModelProviders.of(this).get(ArticleGroupViewModel.class);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        List<ItemsWithCount> itemsWithCount = new ArrayList<ItemsWithCount>();

        allItemsAdapter = new AllItemsAdapter(itemsWithCount, this);

        allItemsList = findViewById(R.id.allItemsList);
        allItemsList.setLayoutManager(new LinearLayoutManager(this));

        // Set custom adapter
        allItemsList.setAdapter(allItemsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        allItemsList.addItemDecoration(dividerItemDecoration);

        // Get Inventory Items out of the database and watch changes
        articleGroupViewModel.getItemsWithCount().observe(this, new Observer<List<ItemsWithCount>>() {
            @Override
            public void onChanged(List<ItemsWithCount> itemsWithCount) {
                allItemsAdapter.setAllItems(itemsWithCount);
            }
        });

        // Initialize ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(allItemsList);
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
        articleGroupViewModel.getItemsWithCountSearch(name).observe(AllItemsActivity.this, new Observer<List<ItemsWithCount>>() {
            @Override
            public void onChanged(List<ItemsWithCount> itemsWithCounts) {
                allItemsAdapter.setAllItems(itemsWithCounts);
            }
        });
    }

    /**
     * Add swipe actions to allItemsList
     * */
    private ItemsWithCount currenItem = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();
            currenItem = allItemsAdapter.getItemsWithCountAt(position);

            /*
             * If the user swipes to the right and to the left direction the article will be inserted into the inventory
             * */

            ItemsWithCount itemsWithCount = allItemsAdapter.getItemsWithCountAt(position);
            List<Long> inventorId = mainViewModel.insertInventorybyGroupId(itemsWithCount.getGroupId());

            /*
             * Undo the whole process
             *
             * The user can press the Undo button to undo all changes
             * */
            Snackbar.make(allItemsList, currenItem.getName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainViewModel.delete(mainViewModel.getItemById(inventorId.get(0).intValue()));
                }
            }).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();

        }

        /**
         * Background design of the inventoryListSwipeActions
         * */
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(AllItemsActivity.this, R.color.reptile_green))
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_library_add_24)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(AllItemsActivity.this, R.color.reptile_green))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_library_add_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void onItemClick(int position) {

    }
}