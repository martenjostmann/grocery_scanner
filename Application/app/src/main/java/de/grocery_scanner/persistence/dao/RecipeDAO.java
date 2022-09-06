package de.grocery_scanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.grocery_scanner.persistence.elements.Recipe;


@Dao
public interface RecipeDAO {
    @Insert
    public void insert(Recipe... items);

    @Update
    public void update(Recipe... items);
    @Delete
    public void delete(Recipe item);

    @Query("SELECT * FROM recipe")
    public List<Recipe> getAllRecipes();
}
