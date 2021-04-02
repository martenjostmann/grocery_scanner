package de.grocery_scanner.persistence.dao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
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

    @Query("SELECT * " + "FROM inventory " + "INNER JOIN ean USING (eanId)")
    public List<inventoryEan> getInventory();

    @Query("SELECT * FROM inventory WHERE inventoryId = :id")
    public inventory getItemById(int id);

    @Query("SELECT * FROM inventory WHERE eanId = :ean")
    public inventory getItemByEanId(String ean);

    @Query("SELECT count(*) FROM inventory WHERE inventoryId = :id")
    public int checkInventory(String id);

    @Query("SELECT count(*) FROM inventory")
    public int checkIfEmpty();


    //Joined Tables class to access data
    static class inventoryEan {
        public String eanId;
        public String name;
        public int inventoryId;
        public Date inDate;
        public Date outDate;
        public int use;
        public int allocationTime;

        public String getEanId() {
            return eanId;
        }

        public String getName() {
            return name;
        }

        public int getInventoryId() {
            return inventoryId;
        }

        public Date getInDate() {
            return inDate;
        }

        public Date getOutDate() {
            return outDate;
        }

        public int getUse() {
            return use;
        }

        public int getAllocationTime() {
            return allocationTime;
        }
    }
}
