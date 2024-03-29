package de.grocery_scanner.inventory;


import android.graphics.Canvas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import de.grocery_scanner.AppDatabase;

import de.grocery_scanner.inventory.filter.Group;
import de.grocery_scanner.inventory.filter.Sort;
import de.grocery_scanner.persistence.dao.InventoryDAO;
import de.grocery_scanner.viewmodel.MainViewModel;
import de.grocery_scanner.R;
import de.grocery_scanner.persistence.elements.Inventory;

import de.grocery_scanner.persistence.dao.InventoryDAO.InventoryArticleGroup;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;




public class InventoryFragment extends Fragment implements InventoryAdapter.OnItemListener {

    private MainViewModel mainViewModel;
    private RecyclerView inventoryList;
    private InventoryAdapter inventoryAdapter;
    private Sort sort = Sort.dateDESC;
    private Group group = Group.none;

    public InventoryFragment() {
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

        List<InventoryArticleGroup> inventory = new ArrayList<InventoryArticleGroup>();  //initialize inventory

        inventoryAdapter = new InventoryAdapter(inventory, this);

        inventoryList = getView().findViewById(R.id.inventoryList);
        inventoryList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set custom adapter
        inventoryList.setAdapter(inventoryAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        inventoryList.addItemDecoration(dividerItemDecoration);

        // Get Inventory Items out of the database and watch changes
        mainViewModel.getInventory().observe(getViewLifecycleOwner(), new Observer<List<InventoryArticleGroup>>() {
            @Override
            public void onChanged(List<InventoryArticleGroup> inventoryArticleGroups) {

                inventoryAdapter.setInventory(inventoryArticleGroups);
                inventoryAdapter.sortInventory(sort);   // Apply sort to updated list
            }
        });

        // Initialize ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(inventoryList);


    }



    /**
    * Add swipe actions to inventoryList
    * */
    private InventoryArticleGroup currenItem = null;

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
                    InventoryArticleGroup inventoryArticleGroupItem = inventoryAdapter.getInventoryAt(position);
                    mainViewModel.delete(mainViewModel.getItemById(inventoryArticleGroupItem.inventoryId)); //delete item from database
                    inventoryAdapter.removeInventoryAt(position);     //remove item from the list

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
                            Inventory inventoryItem = new Inventory();
                            inventoryItem.setGroupId(currenItem.getGroupId());
                            inventoryItem.setInDate(currenItem.getInDate());
                            inventoryItem.setInventoryId(currenItem.getInventoryId());
                            inventoryItem.setUse(currenItem.getUse());
                            mainViewModel.insert(inventoryItem);

                        }
                    }).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();

                    break;
                case ItemTouchHelper.RIGHT:
                    /*
                    * If the user swipes to the right direction the inventory item will be used (use increase)
                    * */

                    inventoryArticleGroupItem = inventoryAdapter.getInventoryAt(position);
                    inventoryArticleGroupItem.use++;  //increase itemNumber
                    Inventory inventoryItem = mainViewModel.getItemById(inventoryArticleGroupItem.inventoryId);    //get current item out of the database
                    inventoryItem.setUse(inventoryArticleGroupItem.use);  //increase itemNumber
                    mainViewModel.update(inventoryItem);     //update item in the database

                    /*
                    * Undo the whole process
                    *
                    * The user can press the Undo button to undo all changes
                    * */
                    Snackbar.make(inventoryList, currenItem.getName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            InventoryArticleGroup inventoryArticleGroupItem = inventoryAdapter.getInventoryAt(position);
                            inventoryArticleGroupItem.use--;
                            Inventory inventoryItem = mainViewModel.getItemById(inventoryArticleGroupItem.inventoryId);
                            inventoryItem.setUse(inventoryArticleGroupItem.use);

                            mainViewModel.update(inventoryItem);
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
        sort = filter.getSort();
        inventoryAdapter.sortInventory(sort);
    }

    @Override
    public void onItemClick(int position) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        InventoryDialog dialog = new InventoryDialog();
        dialog.show(fragmentManager,"tag");
    }
}