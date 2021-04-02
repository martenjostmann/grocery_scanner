package de.grocery_scanner.inventory;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.List;

import de.grocery_scanner.AppDatabase;
import de.grocery_scanner.R;
import de.grocery_scanner.persistence.dao.inventoryDAO;
import de.grocery_scanner.persistence.elements.inventory;
import de.grocery_scanner.persistence.instantiateDatabase;
import de.grocery_scanner.persistence.dao.inventoryDAO.inventoryEan;

import static android.icu.text.DisplayContext.LENGTH_SHORT;


public class inventoryFragment extends Fragment{

    private inventoryDAO inventoryDAO;
    private AppDatabase database;
    private List<inventoryEan> inventory;
    private inventoryEan[] inventoryArray;
    private SwipeMenuListView inventoryList;
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
        // initiate a new database connection
        database = Room.databaseBuilder(getActivity(), AppDatabase.class, "mydb")
                .allowMainThreadQueries()
                .build();

        inventoryDAO = database.getInventoryDAO();
        inventory =  inventoryDAO.getInventory();



        inventoryAdapter = new inventoryAdapter(getActivity(), inventory);
        inventoryList = getView().findViewById(R.id.inventoryList);
        inventoryList.setAdapter(inventoryAdapter);



        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                //deleteItem.setIcon(R.drawable.ic_baseline_delete_24);
                deleteItem.setIcon(getContext().getDrawable(R.drawable.ic_baseline_delete_24));
                // add to menu
                menu.addMenuItem(deleteItem);

                // create "use" item
                SwipeMenuItem useItem = new SwipeMenuItem(
                        getContext());
                // set item background
                useItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                useItem.setWidth(500);
                // set a icon
                useItem.setTitle("Benutzen");
                useItem.setTitleSize(18);
                // set item title font color
                useItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(useItem);
            }
        };

        // set creator
        inventoryList.setMenuCreator(creator);


        // Right
        inventoryList.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        inventoryList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:

                        return false;

                    case 1:
                        Log.d("TAG", "onMenuItemClick: " + position);

                        inventoryEan currentItem = inventory.get(position);
                        String eanId = currentItem.eanId;
                        int use = currentItem.use;
                        inventory inventoryItem = inventoryDAO.getItemByEanId(eanId);
                        inventoryItem.setUse(use + 1);
                        inventoryDAO.update(inventoryItem);


                        
                        return false;

                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        inventoryList.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {

                inventoryList.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {

            }
        });


    }

    private void updateData(){
        inventory.get(1).use = 100;
        inventoryAdapter.notifyDataSetChanged();

        Toast.makeText(getContext(),"Item changed",Toast.LENGTH_SHORT).show();

    }
}