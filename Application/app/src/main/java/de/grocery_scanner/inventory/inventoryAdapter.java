package de.grocery_scanner.inventory;

import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import de.grocery_scanner.R;
import de.grocery_scanner.persistence.dao.inventoryDAO;
import de.grocery_scanner.persistence.elements.inventory;

public class inventoryAdapter extends ArrayAdapter<inventoryDAO.inventoryEan> {

    private final Activity context;
    private List<inventoryDAO.inventoryEan> inventory;

    public inventoryAdapter(Activity context,List<inventoryDAO.inventoryEan> inventory) {
        super(context, R.layout.fragment_inventory_listitem, inventory);
        this.context = context;
        this.inventory = inventory;
    }

    public void remove(int position) {
        inventory.remove(position);

        notifyDataSetChanged();
    }

    public void setInventory(List<inventoryDAO.inventoryEan> inventory) {
        this.inventory = inventory;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.fragment_inventory_listitem, null, true);

        ConstraintLayout constraintLayout = rowView.findViewById(R.id.inventory_table_row);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView use = (TextView) rowView.findViewById(R.id.use);
        TextView inDateText = (TextView) rowView.findViewById(R.id.inDateText);

        name.setText(inventory.get(i).getName());
        use.setText("" + inventory.get(i).getUse());


        Date inDate = inventory.get(i).getInDate();
        String inDateString = transformDate(inDate);

        inDateText.setText(inDateString);

        return rowView;
    }

    private String transformDate(Date date){
        String day = (String) DateFormat.format("dd",  date);
        String monthNumber = (String) DateFormat.format("MM",   date);
        String year = (String) DateFormat.format("yyyy", date);

        return(day +"."+ monthNumber + "." + year);
    }

    public List<inventoryDAO.inventoryEan> getData(){
        return inventory;
    }

}
