package de.grocery_scanner.persistence.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import de.grocery_scanner.persistence.elements.Ean;

@Dao
public interface EanDAO {

    @Insert
    public void insert(Ean... items);

    @Update
    public void update(Ean... items);
    @Delete
    public void delete(Ean item);

    @Query("SELECT * FROM Ean")
    public List<Ean> getAllEan();

    @Query("SELECT * FROM Ean WHERE eanId = :id")
    public Ean getItemById(String id);

    @Query("SELECT count(*) FROM Ean WHERE eanId = :id")
    public int checkEan(String id);

    @Query("SELECT count(*) FROM Ean")
    public int checkIfEanEmpty();

}
