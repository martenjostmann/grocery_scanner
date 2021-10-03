package de.grocery_scanner.inventory.filter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.grocery_scanner.MainActivity;
import de.grocery_scanner.R;
import de.grocery_scanner.inventory.InventoryFilter;
import de.grocery_scanner.persistence.elements.Inventory;


public class FilterBottomSheet extends BottomSheetDialogFragment {

    private MaterialButtonToggleGroup sortToggleButtonR1;
    private MaterialButtonToggleGroup sortToggleButtonR2;
    private MaterialButtonToggleGroup groupToggleButtonR1;
    private MaterialButtonToggleGroup groupToggleButtonR2;
    private MaterialButtonToggleGroup.OnButtonCheckedListener sortR1Listener;
    private MaterialButtonToggleGroup.OnButtonCheckedListener sortR2Listener;
    private MaterialButtonToggleGroup.OnButtonCheckedListener groupR1Listener;
    private MaterialButtonToggleGroup.OnButtonCheckedListener groupR2Listener;

    private InventoryFilter filter;

    public FilterBottomSheet(InventoryFilter filter) {
        this.filter = filter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory_filter_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View bottomSheet = view.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


        // Define Buttons
        Button btnSend = view.findViewById(R.id.btn_send);
        Button btnReset = view.findViewById(R.id.reset);

        // Sort Buttons
        sortToggleButtonR1 = view.findViewById(R.id.sortToggleButtonR1);
        sortToggleButtonR2 = view.findViewById(R.id.sortToggleButtonR2);

        // Group Buttons
        groupToggleButtonR1 = view.findViewById(R.id.groupToggleButtonR1);
        groupToggleButtonR2 = view.findViewById(R.id.groupToggleButtonR2);

        // Restore current filter
        restoreFilter();

        // Reset to default values
        btnReset.setOnClickListener(v -> {
            sortToggleButtonR1.check(R.id.dateDESC);
            groupToggleButtonR1.check(R.id.groupNull);
        });


        // Sort Listeners
        sortR1Listener = (group, checkedId, isChecked) -> {

            // Control single select over multiple Toggle Groups
            sortToggleButtonR2.removeOnButtonCheckedListener(sortR2Listener);
            sortToggleButtonR2.clearChecked();
            sortToggleButtonR2.addOnButtonCheckedListener(sortR2Listener);

            if (isChecked) {
                if(checkedId == R.id.dateDESC){
                    filter.setSort(Sort.dateDESC);
                }else if(checkedId == R.id.dateASC){
                    filter.setSort(Sort.dateASC);
                }
            }
        };

        sortR2Listener = (group, checkedId, isChecked) -> {

            // Control single select over multiple Toggle Groups
            sortToggleButtonR1.removeOnButtonCheckedListener(sortR1Listener);
            sortToggleButtonR1.clearChecked();
            sortToggleButtonR1.addOnButtonCheckedListener(sortR1Listener);

            if (isChecked) {
                if(checkedId == R.id.useDESC){
                    filter.setSort(Sort.useDESC);
                }else if(checkedId == R.id.useASC){
                    filter.setSort(Sort.useASC);
                }
            }
        };

        // Group Listeners
        groupR1Listener = (group, checkedId, isChecked) -> {

            // Control single select over multiple Toggle Groups
            groupToggleButtonR2.removeOnButtonCheckedListener(groupR2Listener);
            groupToggleButtonR2.clearChecked();
            groupToggleButtonR2.addOnButtonCheckedListener(groupR2Listener);

            if (isChecked) {
                if(checkedId == R.id.groupNull){
                    filter.setGroup(Group.none);
                }else if(checkedId == R.id.groupArticle){
                    filter.setGroup(Group.article);
                }
            }
        };

        groupR2Listener = (group, checkedId, isChecked) -> {

            // Control single select over multiple Toggle Groups
            groupToggleButtonR1.removeOnButtonCheckedListener(groupR1Listener);
            groupToggleButtonR1.clearChecked();
            groupToggleButtonR1.addOnButtonCheckedListener(groupR1Listener);

            if (isChecked) {
                if(checkedId == R.id.groupDate){
                    filter.setGroup(Group.date);
                }else if(checkedId == R.id.groupUse){
                    filter.setGroup(Group.use);
                }
            }
        };


        // Add Listeners
        sortToggleButtonR1.addOnButtonCheckedListener(sortR1Listener);
        sortToggleButtonR2.addOnButtonCheckedListener(sortR2Listener);
        groupToggleButtonR1.addOnButtonCheckedListener(groupR1Listener);
        groupToggleButtonR2.addOnButtonCheckedListener(groupR2Listener);




        btnSend.setOnClickListener(v -> {

            MainActivity activity = (MainActivity) getActivity();

            activity.onSendClicked(filter);
            dismiss();
        });


    }

    /**
     * Method to restore previously set filter settings
     * */
    public void restoreFilter(){
        switch (filter.getSort()) {
            case dateDESC:
                sortToggleButtonR1.check(R.id.dateDESC);
                break;
            case dateASC:
                sortToggleButtonR1.check(R.id.dateASC);
                break;
            case useDESC:
                sortToggleButtonR2.check(R.id.useDESC);
                break;
            case useASC:
                sortToggleButtonR2.check(R.id.useASC);
                break;
        }

        switch (filter.getGroup()) {
            case none:
                groupToggleButtonR1.check(R.id.groupNull);
                break;
            case article:
                groupToggleButtonR1.check(R.id.groupArticle);
                break;
            case date:
                groupToggleButtonR2.check(R.id.groupDate);
                break;
            case use:
                groupToggleButtonR2.check(R.id.groupUse);
                break;
        }
    }


    public interface BottomSheetListener{
        void onSendClicked(InventoryFilter filter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            BottomSheetListener mListener = (BottomSheetListener) getActivity();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
            + " must implement BottomSheetListener");
        }
    }


}
