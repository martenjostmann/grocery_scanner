package de.grocery_scanner.helper.insertean;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import de.grocery_scanner.R;
import de.grocery_scanner.persistence.elements.ArticleGroup;

public class ArticleGroupAdapter extends RecyclerView.Adapter<ArticleGroupAdapter.ViewHolder>{

    private List<ArticleGroup> articleGroups;
    private List<ArticleGroup> filteredArticleGroups;
    private OnItemListener onItemListener;
    private int selectedPosition = -1;

    //private ItemFilter mFilter = new ItemFilter();

    public ArticleGroupAdapter(List<ArticleGroup> articleGroups, List<ArticleGroup> filteredArticleGroups, OnItemListener onItemListener) {
        this.articleGroups = articleGroups;
        this.filteredArticleGroups = articleGroups;
        this.onItemListener = onItemListener;
    }


    @NonNull
    @Override
    public ArticleGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.insert_ean_article_group_listitem, parent, false);

        ViewHolder viewHolder = new ViewHolder(rowView, onItemListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleGroupAdapter.ViewHolder holder, int position) {
        holder.groupButton.setText(filteredArticleGroups.get(position).getName());
        holder.groupButton.setChecked(position == selectedPosition);

        holder.groupButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {

                    selectedPosition = holder.getAdapterPosition();
                    onItemListener.onItemClick(filteredArticleGroups.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredArticleGroups.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RadioButton groupButton;
        OnItemListener onItemListener;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener){
            super(itemView);

            groupButton = itemView.findViewById(R.id.groupButton);
            this.onItemListener = onItemListener;

        }

    }

    public interface OnItemListener{
        void onItemClick(ArticleGroup articleGroup);
    }

    public void setArticleGroups(List<ArticleGroup> articleGroups) {
        this.articleGroups = articleGroups;
        notifyDataSetChanged();
    }

    public List<ArticleGroup> getArticleGroups() {
       return this.articleGroups;
    }

    public void setFilteredArticleGroups(List<ArticleGroup> filteredArticleGroups) {
        this.filteredArticleGroups = filteredArticleGroups;
        notifyDataSetChanged();
    }


}
