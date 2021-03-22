package de.grocery_scanner;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import de.grocery_scanner.persistence.dao.eanDAO;
import de.grocery_scanner.persistence.elements.ean;


@Database(entities = {ean.class},version = 1)

public abstract class AppDatabase extends RoomDatabase {

    public abstract eanDAO getEanDAO();

}
