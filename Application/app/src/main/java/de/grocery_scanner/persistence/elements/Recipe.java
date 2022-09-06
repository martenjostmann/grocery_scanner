package de.grocery_scanner.persistence.elements;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipe")
public class Recipe {

    @PrimaryKey()
    @NonNull
    private Long recipeId;

    @NonNull
    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(@NonNull Long recipeId) {
        this.recipeId = recipeId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    private String name;
}
