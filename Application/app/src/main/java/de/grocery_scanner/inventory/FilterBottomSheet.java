package de.grocery_scanner.inventory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.grocery_scanner.MainActivity;
import de.grocery_scanner.R;


public class FilterBottomSheet extends BottomSheetDialogFragment {

    private MaterialButtonToggleGroup sortToggleButtonR1;
    private MaterialButtonToggleGroup sortToggleButtonR2;
    private MaterialButtonToggleGroup groupToggleButtonR1;
    private MaterialButtonToggleGroup groupToggleButtonR2;
    private MaterialButtonToggleGroup.OnButtonCheckedListener sortR1Listener;
    private MaterialButtonToggleGroup.OnButtonCheckedListener sortR2Listener;
    private MaterialButtonToggleGroup.OnButtonCheckedListener groupR1Listener;
    private MaterialButtonToggleGroup.OnButtonCheckedListener groupR2Listener;

    private BottomSheetListener mListener;
    private FloatingActionButton btn_send;
    private Sort sort;
    private Group _group;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inventory_filter_dialog, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Define Buttons
        btn_send = getView().findViewById(R.id.btn_send);

        // Sort Buttons
        sortToggleButtonR1 = view.findViewById(R.id.sortToggleButtonR1);
        sortToggleButtonR2 = view.findViewById(R.id.sortToggleButtonR2);

        // Group Buttons
        groupToggleButtonR1 = view.findViewById(R.id.groupToggleButtonR1);
        groupToggleButtonR2 = view.findViewById(R.id.groupToggleButtonR2);


        // Sort Listeners
        sortR1Listener = new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                // Control single select over multiple Toggle Groups
                sortToggleButtonR2.removeOnButtonCheckedListener(sortR2Listener);
                sortToggleButtonR2.clearChecked();
                sortToggleButtonR2.addOnButtonCheckedListener(sortR2Listener);

                if (isChecked) {
                    if(checkedId == R.id.dateDESC){
                        sort = Sort.dateDESC;
                    }else if(checkedId == R.id.dateASC){
                        sort = Sort.dateASC;
                    }
                }
            }
        };

        sortR2Listener = new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                // Control single select over multiple Toggle Groups
                sortToggleButtonR1.removeOnButtonCheckedListener(sortR1Listener);
                sortToggleButtonR1.clearChecked();
                sortToggleButtonR1.addOnButtonCheckedListener(sortR1Listener);

                if (isChecked) {
                    if(checkedId == R.id.useDESC){
                        sort = Sort.useDESC;
                    }else if(checkedId == R.id.useASC){
                        sort = Sort.useASC;
                    }
                }
            }
        };

        // Group Listeners
        groupR1Listener = new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                // Control single select over multiple Toggle Groups
                groupToggleButtonR2.removeOnButtonCheckedListener(groupR2Listener);
                groupToggleButtonR2.clearChecked();
                groupToggleButtonR2.addOnButtonCheckedListener(groupR2Listener);

                if (isChecked) {
                    if(checkedId == R.id.groupNull){
                        _group = Group.none;
                    }else if(checkedId == R.id.groupArticle){
                        _group = Group.article;
                    }
                }
            }
        };

        groupR2Listener = new MaterialButtonToggleGroup.OnButtonCheckedListener() {
            @Override
            public void onButtonChecked(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {

                // Control single select over multiple Toggle Groups
                groupToggleButtonR1.removeOnButtonCheckedListener(groupR1Listener);
                groupToggleButtonR1.clearChecked();
                groupToggleButtonR1.addOnButtonCheckedListener(groupR1Listener);

                if (isChecked) {
                    if(checkedId == R.id.groupDate){
                        _group = Group.date;
                    }else if(checkedId == R.id.groupUse){
                        _group = Group.use;
                    }
                }
            }
        };


        // Add Listeners
        sortToggleButtonR1.addOnButtonCheckedListener(sortR1Listener);
        sortToggleButtonR2.addOnButtonCheckedListener(sortR2Listener);
        groupToggleButtonR1.addOnButtonCheckedListener(groupR1Listener);
        groupToggleButtonR2.addOnButtonCheckedListener(groupR2Listener);




        btn_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                MainActivity activity = (MainActivity) getActivity();
                InventoryFilter inventoryFilter = new InventoryFilter(sort, _group);

                activity.onSendClicked(inventoryFilter);
                dismiss();
            }
        });


    }


    public interface BottomSheetListener{
        void onSendClicked(InventoryFilter filter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mListener = (BottomSheetListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
            + " must implement BottomSheetListener");
        }
    }
}
