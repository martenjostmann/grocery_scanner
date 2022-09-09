package de.grocery_scanner.actionMenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.grocery_scanner.R;
import de.grocery_scanner.persistence.dao.ArticleGroupDAO;
import de.grocery_scanner.persistence.dao.EanDAO;


public class AllItemsAdapter extends RecyclerView.Adapter<AllItemsAdapter.ViewHolder>{

    private List<ArticleGroupDAO.ItemsWithCount> itemsWithCount;
    private OnItemListener onItemListener;

    public AllItemsAdapter(List<ArticleGroupDAO.ItemsWithCount> itemsWithCount, OnItemListener onItemListener) {
        this.itemsWithCount = itemsWithCount;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.all_items_listitem, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView, onItemListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(itemsWithCount.get(position).getName());
        holder.num_in_inventory.setText("" + itemsWithCount.get(position).getNumInInventory());
    }

    @Override
    public int getItemCount() {
        return itemsWithCount.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name, num_in_inventory;
        OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener){
            super(itemView);

            name = itemView.findViewById(R.id.name);
            num_in_inventory = itemView.findViewById(R.id.num_in_inventory);
            this.onItemListener = onItemListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public void setAllItems(List<ArticleGroupDAO.ItemsWithCount> itemsWithCount) {
        this.itemsWithCount = itemsWithCount;
        notifyDataSetChanged();
    }

    public interface OnItemListener{
        void onItemClick(int position);
    }
}
