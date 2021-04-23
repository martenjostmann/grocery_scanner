package de.grocery_scanner;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.grocery_scanner.persistence.converters;
import de.grocery_scanner.persistence.dao.eanDAO;
import de.grocery_scanner.persistence.dao.inventoryDAO;
import de.grocery_scanner.persistence.elements.ean;
import de.grocery_scanner.persistence.elements.inventory;


@Database(entities = {ean.class, inventory.class}, version = 3)
@TypeConverters({converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract eanDAO getEanDAO();
    public abstract inventoryDAO getInventoryDAO();

}
