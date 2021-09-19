package de.grocery_scanner.persistence.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.grocery_scanner.persistence.elements.ean;

@Dao
public interface eanDAO {

    @Insert
    public void insert(ean... items);

    @Update
    public void update(ean... items);
    @Delete
    public void delete(ean item);

    @Query("SELECT * FROM ean")
    public List<ean> getAllEan();

    @Query("SELECT * FROM ean WHERE eanId = :id")
    public ean getItemById(String id);

    @Query("SELECT count(*) FROM ean WHERE eanId = :id")
    public int checkEan(String id);

    @Query("SELECT count(*) FROM ean")
    public int checkIfEanEmpty();
}
