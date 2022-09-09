package de.grocery_scanner.inventory;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.grocery_scanner.R;
import de.grocery_scanner.inventory.filter.Sort;
import de.grocery_scanner.persistence.dao.InventoryDAO;


public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {

    private List<InventoryDAO.InventoryArticleGroup> inventory;
    private OnItemListener onItemListener;

    public InventoryAdapter(List<InventoryDAO.InventoryArticleGroup> inventory, OnItemListener onItemListener) {
        this.inventory = inventory;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.fragment_inventory_listitem, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView, onItemListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(inventory.get(position).getName());
        holder.use.setText("" + inventory.get(position).getUse());


        Date inDate = inventory.get(position).getInDate();
        String inDateString = transformDate(inDate);

        holder.inDateText.setText(inDateString);
    }

    @Override
    public int getItemCount() {
        return inventory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name, use, inDateText;
        OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener){
            super(itemView);

            name = itemView.findViewById(R.id.name);
            use = itemView.findViewById(R.id.use);
            inDateText = itemView.findViewById(R.id.inDateText);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }

    public void setInventory(List<InventoryDAO.InventoryArticleGroup> inventory) {
        this.inventory = inventory;
        notifyDataSetChanged();
    }

    public List<InventoryDAO.InventoryArticleGroup> getInventory() {
        return inventory;
    }

    public InventoryDAO.InventoryArticleGroup getInventoryAt(int position) {
        return inventory.get(position);
    }

    public void addInventoryAt(int position, InventoryDAO.InventoryArticleGroup inventoryItem) {
        inventory.add(position, inventoryItem);
    }

    public InventoryDAO.InventoryArticleGroup removeInventoryAt(int position) {
        return inventory.remove(position);
    }

    public void sortInventory(Sort sort) {
        switch (sort) {
            case dateDESC:
                inventory.sort((left, right) -> (int) (right.getInDate().getTime() - left.getInDate().getTime()));
                break;
            case dateASC:
                inventory.sort((left, right) -> (int) (left.getInDate().getTime() - right.getInDate().getTime()));
                break;
            case useDESC:
                inventory.sort((left, right) -> right.getUse() - left.getUse());
                break;
            case useASC:
                inventory.sort((left, right) -> left.getUse() - right.getUse());
                break;
        }
        notifyDataSetChanged();
    }

    private String transformDate(Date date){
        String day = (String) DateFormat.format("dd",  date);
        String monthNumber = (String) DateFormat.format("MM",   date);
        String year = (String) DateFormat.format("yyyy", date);

        return(day +"."+ monthNumber + "." + year);
    }


}
