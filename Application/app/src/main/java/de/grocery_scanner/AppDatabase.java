package de.grocery_scanner;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import de.grocery_scanner.persistence.Converters;
import de.grocery_scanner.persistence.dao.ArticleGroupDAO;
import de.grocery_scanner.persistence.dao.EanDAO;
import de.grocery_scanner.persistence.dao.InventoryDAO;
import de.grocery_scanner.persistence.dao.RecipeDAO;
import de.grocery_scanner.persistence.elements.ArticleGroup;
import de.grocery_scanner.persistence.elements.Ean;
import de.grocery_scanner.persistence.elements.Inventory;
import de.grocery_scanner.persistence.elements.Recipe;


@Database(entities = {Ean.class, Inventory.class, ArticleGroup.class, Recipe.class}, version = 4)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract EanDAO getEanDAO();
    public abstract InventoryDAO getInventoryDAO();
    public abstract ArticleGroupDAO getArticleGroupDAO();
    public abstract RecipeDAO getRecipeDAO();

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
