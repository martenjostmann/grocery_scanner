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

    @Query("SELECT eanId, name, COALESCE(numInInventory, 0) AS numInInventory FROM Ean LEFT JOIN (SELECT eanId, outDate, count(*) AS 'numInInventory' FROM INVENTORY WHERE outDate IS NULL GROUP BY eanId) USING (eanId)")
    public LiveData<List<ItemsWithCount>> getItemsWithCount();

    @Query("SELECT eanId, name, COALESCE(numInInventory, 0) AS numInInventory " +
            "FROM Ean LEFT JOIN (SELECT eanId, outDate, count(*) AS 'numInInventory' FROM INVENTORY WHERE outDate IS NULL GROUP BY eanId) " +
            "USING (eanId)" +
            "WHERE name LIKE '%' || :name || '%'")
    public LiveData<List<ItemsWithCount>> getItemsWithCountSearch(String name);

    static class ItemsWithCount {
        private String eanId;
        private String name;
        private int numInInventory;

        public String getEanId() {
            return eanId;
        }

        public void setEanId(String eanId) {
            this.eanId = eanId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNumInInventory() {
            return numInInventory;
        }

        public void setNumInInventory(int numInInventory) {
            this.numInInventory = numInInventory;
        }
    }
}
