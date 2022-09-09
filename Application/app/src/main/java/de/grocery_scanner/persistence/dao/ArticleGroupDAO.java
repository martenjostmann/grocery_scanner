package de.grocery_scanner.persistence.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.grocery_scanner.persistence.elements.ArticleGroup;


@Dao
public interface ArticleGroupDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(ArticleGroup... items);

    @Update
    public void update(ArticleGroup... items);
    @Delete
    public void delete(ArticleGroup item);

    @Query("SELECT * FROM articleGroup")
    public LiveData<List<ArticleGroup>> getAllArticleGroups();

    @Query("SELECT groupId, name, COALESCE(numInInventory, 0) AS numInInventory FROM ArticleGroup " +
            "LEFT JOIN (SELECT groupId, outDate, count(*) AS 'numInInventory' FROM INVENTORY WHERE outDate IS NULL GROUP BY groupId) USING (groupId)")
    public LiveData<List<ItemsWithCount>> getItemsWithCount();

    @Query("SELECT groupId, name, COALESCE(numInInventory, 0) AS numInInventory " +
            "FROM ArticleGroup LEFT JOIN (SELECT groupId, outDate, count(*) AS 'numInInventory' FROM INVENTORY WHERE outDate IS NULL GROUP BY groupId) " +
            "USING (groupId)" +
            "WHERE name LIKE '%' || :name || '%'")
    public LiveData<List<ItemsWithCount>> getItemsWithCountSearch(String name);

    static class ItemsWithCount {
        private Long groupId;
        private String name;
        private int numInInventory;

        public Long getGroupId() {
            return groupId;
        }

        public void setGroupId(Long groupId) {
            this.groupId = groupId;
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
