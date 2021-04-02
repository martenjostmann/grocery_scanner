package de.grocery_scanner.inventory;


import android.graphics.Canvas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import de.grocery_scanner.AppDatabase;

import de.grocery_scanner.R;
import de.grocery_scanner.persistence.dao.inventoryDAO;
import de.grocery_scanner.persistence.elements.inventory;

import de.grocery_scanner.persistence.dao.inventoryDAO.inventoryEan;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;




public class inventoryFragment extends Fragment{

    private inventoryDAO inventoryDAO;
    private AppDatabase database;
    private List<inventoryEan> inventory;
    private inventoryEan[] inventoryArray;
    private RecyclerView inventoryList;
    private inventoryAdapter inventoryAdapter;

    public inventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        inventory = new ArrayList<inventoryEan>();  //initialize inventory
        inventoryAdapter = new inventoryAdapter(inventory);

        inventoryList = getView().findViewById(R.id.inventoryList);
        inventoryList.setLayoutManager(new LinearLayoutManager(getContext()));

        //set custom adapter
        inventoryList.setAdapter(inventoryAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        inventoryList.addItemDecoration(dividerItemDecoration);

        // initiate a new database connection
        database = Room.databaseBuilder(getActivity(), AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();

        //get inventory out of the database and fill the inventoryList
        inventoryDAO = database.getInventoryDAO();
        inventory.addAll(inventoryDAO.getInventory());

        //initialize ItemTouchHelper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(inventoryList);

    }


    /*
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
            currenItem = inventory.get(position);

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    /*
                     * If the user swipes to the left direction the inventory item will be deleted
                     * */
                    inventory.remove(position);     //remove item from the list
                    //@Todo remove Item from the database
                    inventoryAdapter.notifyItemRemoved(position);   //notify the adapter that an item has changed
                    /*
                     * Undo the whole process
                     *
                     * The user can press the Undo button to undo all changes
                     * */
                    Snackbar.make(inventoryList, currenItem.getName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inventory.add(position, currenItem);
                            inventoryAdapter.notifyItemInserted(position);
                        }
                    }).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();

                    break;
                case ItemTouchHelper.RIGHT:
                    /*
                    * If the user swipes to the right direction the inventory item will be used (use increase)
                    * */
                    inventory.get(position).use++;  //increase itemNumber
                    inventory inventoryItem = inventoryDAO.getItemById(inventory.get(position).inventoryId);    //get current item out of the database
                    inventoryItem.setUse(inventory.get(position).use);  //increase itemNumber
                    inventoryDAO.update(inventoryItem);     //update item in the database
                    inventoryAdapter.notifyDataSetChanged();    //notify the adapter that an item has changed

                    /*
                    * Undo the whole process
                    *
                    * The user can press the Undo button to undo all changes
                    * */
                    Snackbar.make(inventoryList, currenItem.getName(), Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inventory.get(position).use--;
                            inventory inventoryItem = inventoryDAO.getItemById(inventory.get(position).inventoryId);
                            inventoryItem.setUse(inventory.get(position).use);
                            inventoryDAO.update(inventoryItem);
                            inventoryAdapter.notifyDataSetChanged();
                        }
                    }).setActionTextColor(getResources().getColor(R.color.colorPrimary)).show();
                    break;
            }
        }

        /*
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
}