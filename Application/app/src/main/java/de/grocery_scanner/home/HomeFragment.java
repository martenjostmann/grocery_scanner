/**
 * @author Marten Jostmann
 *
 * @Copyright (c) 2021 Marten Jostmann
 * @license MIT License (LICENSE.txt for more details)
 */

package de.grocery_scanner.home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import de.grocery_scanner.AppDatabase;
import de.grocery_scanner.MainActivity;
import de.grocery_scanner.persistence.dao.InventoryDAO;
import de.grocery_scanner.viewmodel.MainViewModel;
import de.grocery_scanner.R;

import de.grocery_scanner.persistence.dao.InventoryDAO.inventoryEan;


public class HomeFragment extends Fragment {

    private MainViewModel mainViewModel;
    private LinearLayout favouriteContainerContent;
    private ConstraintLayout homeBGView;
    private ConstraintLayout transparentBG;
    private TextView favouriteInnerContainerItemHeader;
    private TextView favouriteInnerContainerItemText;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        transparentBG = view.findViewById(R.id.transparentBG);

        // Close FAB when background of the Fragment was clicked
        homeBGView = (ConstraintLayout) view.findViewById(R.id.homeBGView);
        homeBGView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((MainActivity)getActivity()).closeFABMenu();

                return true;
            }
        });

        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        final TextView inventoryContainerContent = view.findViewById(R.id.inventoryContainerContent);

        mainViewModel.getInventoryQuantity().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer inventoryQuantity) {

                inventoryContainerContent.setText(String.format(Locale.getDefault(), "%d", inventoryQuantity));
            }
        });


        favouriteContainerContent = view.findViewById(R.id.favouriteContainerContent);

        //display favourite items
        mainViewModel.getFavourite(3).observe(getViewLifecycleOwner(), new Observer<List<inventoryEan>>() {
            @Override
            public void onChanged(List<inventoryEan> favouriteItems) {

                favouriteContainerContent.removeAllViews();
                //iterate through all favourite items
                for(inventoryEan item : favouriteItems){

                    //get layout
                    View to_add = getLayoutInflater().inflate(R.layout.fragment_home_favourite_inner_container, favouriteContainerContent, false);
                    favouriteInnerContainerItemHeader = to_add.findViewById(R.id.favouriteInnerContainerItemHeader);
                    favouriteInnerContainerItemText = to_add.findViewById(R.id.favouriteInnerContainerItemText);

                    //setup layout
                    favouriteInnerContainerItemHeader.setText(String.valueOf(item.getName().toUpperCase().charAt(0)));
                    favouriteInnerContainerItemText.setText(item.getName());


                    //include layout
                    favouriteContainerContent.addView(to_add);
                }
            }
        });

    }

    public void toggleBackGround(boolean isFABOpen) {

        if (isFABOpen) {
            transparentBG.setVisibility(View.VISIBLE);
            transparentBG.animate().alpha(0.5f);
        }else {
            transparentBG.animate().alpha(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    transparentBG.setVisibility(View.GONE);
                }
            });
        }
    }

}