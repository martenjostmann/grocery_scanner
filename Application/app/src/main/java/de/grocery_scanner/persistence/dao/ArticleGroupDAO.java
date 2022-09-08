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
}
