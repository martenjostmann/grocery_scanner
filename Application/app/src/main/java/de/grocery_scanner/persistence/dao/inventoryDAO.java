package de.grocery_scanner.persistence.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.grocery_scanner.persistence.elements.inventory;

@Dao
public interface inventoryDAO {
    @Insert
    public void insert(inventory... items);

    @Update
    public void update(inventory... items);
    @Delete
    public void delete(inventory item);

    @Query("SELECT * FROM inventory")
    public List<inventory> getInventory();

    @Query("SELECT * FROM inventory WHERE inventoryId = :id")
    public inventory getItemById(String id);

    @Query("SELECT count(*) FROM inventory WHERE inventoryId = :id")
    public int checkInventory(String id);

    @Query("SELECT count(*) FROM inventory")
    public int checkIfEmpty();
}
