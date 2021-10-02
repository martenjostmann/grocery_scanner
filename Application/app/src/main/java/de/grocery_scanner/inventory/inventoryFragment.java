package de.grocery_scanner.inventory;


import android.graphics.Canvas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import de.grocery_scanner.AppDatabase;

import de.grocery_scanner.viewModel.MainViewModel;
import de.grocery_scanner.R;
import de.grocery_scanner.persistence.elements.inventory;

import de.grocery_scanner.persistence.dao.inventoryDAO.inventoryEan;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;




public class inventoryFragment extends Fragment{

    private MainViewModel mainViewModel;
    private AppDatabase database;
    private List<inventoryEan> inventory;
    private inventoryEan[] inventoryArray;
    private RecyclerView inventoryList;
    private inventoryAdapter inventoryAdapter;
    private inventoryEan inventoryEanItem;

    public inventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        inventoryAdapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        inventory = new ArrayList<inventoryEan>();  //initialize inventory

        inventoryAdapter = new inventoryAdapter(inventory);

        inventoryList = getView().findViewById(R.id.inventoryList);
        inventoryList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set custom adapter
        inventoryList.setAdapter(inventoryAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        inventoryList.addItemDecoration(dividerItemDecoration);

        // Get Inventory Items out of the database and watch changes
        mainViewModel.getInventory().observe(getViewLifecycleOwner(), new Observer<List<inventoryEan>>() {
            @Override
            public void onChanged(List<inventoryEan> inventoryEans) {
                inventoryAdapter.setInventory(inventoryEans);
            }
        });

        // Initialize ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(inventoryList);

    }


    /**
    * Add swipe actions to inventoryList
    * */
    private inventoryEan currenItem = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();
            currenItem = inventoryAdapter.getInventoryAt(position);

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    /*
                     * If the user swipes to the left direction the inventory item will be deleted
                     * */
                    inventoryEanItem = inventoryAdapter.getInventoryAt(position);
                    mainViewModel.delete(mainViewModel.getItemById(inventoryEanItem.inventoryId)); //delete item from database
                    inventoryAdapter.removeInventoryAt(position);     //remove item from the list
                    inventoryAdapter.notifyItemRemoved(position);   //notify the adapter that an item has changed
                    /*
                     * Undo the whole process
                     *
                     * The user can press the Undo button to undo all changes
                     * */
                    Snackbar.make(inventoryList, currenItem.getName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inventoryAdapter.addInventoryAt(position, currenItem);

                            //create inventory with currentItem Object
                            inventory inventoryItem = new inventory();
                            inventoryItem.setEanId(currenItem.getEanId());
                            inventoryItem.setInDate(currenItem.getInDate());
                            inventoryItem.setInventoryId(currenItem.getInventoryId());
                            inventoryItem.setUse(currenItem.getUse());
                            mainViewModel.insert(inventoryItem);

                            inventoryAdapter.notifyItemInserted(position);
                        }
                    }).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();

                    break;
                case ItemTouchHelper.RIGHT:
                    /*
                    * If the user swipes to the right direction the inventory item will be used (use increase)
                    * */

                    inventoryEanItem = inventoryAdapter.getInventoryAt(position);
                    inventoryEanItem.use++;  //increase itemNumber
                    inventory inventoryItem = mainViewModel.getItemById(inventoryEanItem.inventoryId);    //get current item out of the database
                    inventoryItem.setUse(inventoryEanItem.use);  //increase itemNumber

                    mainViewModel.update(inventoryItem);     //update item in the database
                    inventoryAdapter.notifyDataSetChanged();    //notify the adapter that an item has changed

                    /*
                    * Undo the whole process
                    *
                    * The user can press the Undo button to undo all changes
                    * */
                    Snackbar.make(inventoryList, currenItem.getName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inventoryEan inventoryEanItem = inventoryAdapter.getInventoryAt(position);
                            inventoryEanItem.use--;
                            inventory inventoryItem = mainViewModel.getItemById(inventoryEanItem.inventoryId);
                            inventoryItem.setUse(inventoryEanItem.use);

                            mainViewModel.update(inventoryItem);
                            inventoryAdapter.notifyDataSetChanged();
                        }
                    }).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();
                    break;
            }
        }

        /**
        * Background design of the inventoryListSwipeActions
        * */
        public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.reptile_green))
                    .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_library_add_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    /**
     * Apply filters from FilterBottomSheet.java
     *
     * @param filter InventoryFilter with necessary information to apply filters
     * */
    public void applyFilter(InventoryFilter filter){
        System.out.println("Selected Filter " + filter.getSort());
    }

}