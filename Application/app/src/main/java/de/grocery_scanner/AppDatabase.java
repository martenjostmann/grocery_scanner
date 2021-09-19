package de.grocery_scanner;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
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

    private static AppDatabase instance;

    public abstract eanDAO getEanDAO();
    public abstract inventoryDAO getInventoryDAO();

    public static synchronized AppDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "grocery_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
