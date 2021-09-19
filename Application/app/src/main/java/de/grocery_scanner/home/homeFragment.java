/**
 * @author Marten Jostmann
 *
 * @Copyright (c) 2021 Marten Jostmann
 * @license MIT License (LICENSE.txt for more details)
 */

package de.grocery_scanner.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import de.grocery_scanner.AppDatabase;
import de.grocery_scanner.viewModel.MainViewModel;
import de.grocery_scanner.R;
import de.grocery_scanner.persistence.dao.inventoryDAO;

import de.grocery_scanner.persistence.dao.inventoryDAO.inventoryEan;


public class homeFragment extends Fragment {

    private MainViewModel mainViewModel;
    private LinearLayout favouriteContainerContent;
    private inventoryDAO inventoryDAO;
    private AppDatabase database;
    private List<inventoryEan> favouriteItems;
    private View view;

    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {


        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        final TextView inventoryContainerContent = view.findViewById(R.id.inventoryContainerContent);

        mainViewModel.getInventoryQuantity().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer inventoryQuantity) {

                inventoryContainerContent.setText(String.format(Locale.getDefault(), "%d", inventoryQuantity));
            }
        });

        this.view = view;

        //display favourite items
        mainViewModel.getFavourite(3).observe(getViewLifecycleOwner(), new Observer<List<inventoryEan>>() {
            @Override
            public void onChanged(List<inventoryEan> favouriteItems) {
                //iterate through all favourite items
                for(inventoryEan item : favouriteItems){
                    LinearLayout favouriteContainerContent = view.findViewById(R.id.favouriteContainerContent);

                    //get layout
                    View to_add = getLayoutInflater().inflate(R.layout.fragment_home_favourite_inner_container, favouriteContainerContent, false);
                    TextView favouriteInnerContainerItemHeader = to_add.findViewById(R.id.favouriteInnerContainerItemHeader);
                    TextView favouriteInnerContainerItemText = to_add.findViewById(R.id.favouriteInnerContainerItemText);

                    //setup layout
                    favouriteInnerContainerItemHeader.setText(String.valueOf(item.getName().toUpperCase().charAt(0)));
                    favouriteInnerContainerItemText.setText(item.getName());

                    //include layout
                    favouriteContainerContent.addView(to_add);
                }
            }
        });





        //Set inventoryQuantity


    }

    private void changetext(Integer inventoryQuantity){
        System.out.println("update inventory quantity");
        TextView inventoryContainerContent = view.findViewById(R.id.inventoryContainerContent);
        inventoryContainerContent.setText(String.format(Locale.getDefault(), "%d", inventoryQuantity));
    }
}