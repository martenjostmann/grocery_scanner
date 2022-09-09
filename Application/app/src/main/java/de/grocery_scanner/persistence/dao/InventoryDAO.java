package de.grocery_scanner.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

import de.grocery_scanner.persistence.elements.Inventory;

@Dao
public interface InventoryDAO {
    @Insert
    public void insert(Inventory... items);

    @Update
    public void update(Inventory... items);
    @Delete
    public void delete(Inventory item);

    @Query("SELECT * " + "FROM Inventory " + "INNER JOIN ArticleGroup USING (groupId)" + "ORDER BY inDate DESC")
    public LiveData<List<InventoryArticleGroup>> getInventory();

    @Query("SELECT * FROM Inventory WHERE inventoryId = :id")
    public Inventory getItemById(int id);

    @Query("SELECT * FROM Inventory WHERE groupId = :groupId")
    public Inventory getItemByGroupId(Long groupId);

    @Query("SELECT count(*) FROM Inventory WHERE inventoryId = :id")
    public int checkInventory(String id);

    @Query("SELECT count(*) FROM Inventory")
    public int checkIfEmpty();

    @Query("SELECT count(*) FROM Inventory WHERE outDate IS null")
    public LiveData<Integer> inventoryQuantity();

    @Query("Select *, count(*) as \"frequencies\"  FROM Inventory INNER JOIN ArticleGroup USING (groupId) Group by groupId ORDER BY \"frequencies\" DESC LIMIT :limit")
    public LiveData<List<InventoryArticleGroup>> getFavourite(int limit);

    //Joined Tables class to access data
    static class InventoryArticleGroup {

        public Long groupId;
        public String name;
        public int inventoryId;
        public Date inDate;
        public Date outDate;
        public int use;
        public int allocationTime;

        public Long getGroupId() {
            return groupId;
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
