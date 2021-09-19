package de.grocery_scanner.inventory;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import de.grocery_scanner.R;
import de.grocery_scanner.persistence.dao.inventoryDAO;


public class inventoryAdapter extends RecyclerView.Adapter<inventoryAdapter.ViewHolder> {

    List<inventoryDAO.inventoryEan> inventory;

    public inventoryAdapter(List<inventoryDAO.inventoryEan> inventory) {
        this.inventory = inventory;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.fragment_inventory_listitem, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView);

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

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, use, inDateText;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.name);
            use = itemView.findViewById(R.id.use);
            inDateText = itemView.findViewById(R.id.inDateText);
        }

    }

    public void setInventory(List<inventoryDAO.inventoryEan> inventory) {
        this.inventory = inventory;
        notifyDataSetChanged();
    }

    public inventoryDAO.inventoryEan getInventoryAt(int position) {
        return inventory.get(position);
    }

    public void addInventoryAt(int position, inventoryDAO.inventoryEan inventoryItem) {
        inventory.add(position, inventoryItem);
    }

    public inventoryDAO.inventoryEan removeInventoryAt(int position) {
        return inventory.remove(position);
    }

    private String transformDate(Date date){
        String day = (String) DateFormat.format("dd",  date);
        String monthNumber = (String) DateFormat.format("MM",   date);
        String year = (String) DateFormat.format("yyyy", date);

        return(day +"."+ monthNumber + "." + year);
    }


}
