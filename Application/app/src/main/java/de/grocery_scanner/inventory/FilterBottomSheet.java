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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.grocery_scanner.R;


public class FilterBottomSheet extends BottomSheetDialogFragment {

    private MaterialButton bnt_use;
    private MaterialButton btn_date;
    private MaterialButton btn_alpha;
    private Boolean useFilter;
    private Boolean dateFilter;
    private Boolean alphaFilter;
    private BottomSheetListener mListener;
    private FloatingActionButton btn_send;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inventory_filter_dialog, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        useFilter = false;
        dateFilter = false;
        alphaFilter = true;

        //bnt_use = getView().findViewById(R.id.bnt_use);
        //btn_date = getView().findViewById(R.id.btn_date);
        //btn_alpha = getView().findViewById(R.id.btn_alpha);
        btn_send = getView().findViewById(R.id.btn_send);




        btn_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mListener.onSendClicked("Hallo Test");
                dismiss();
            }
        });


    }


    public interface BottomSheetListener{
        void onSendClicked(String text);
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
