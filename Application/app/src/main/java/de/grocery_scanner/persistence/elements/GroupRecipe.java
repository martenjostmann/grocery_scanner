package de.grocery_scanner.persistence.elements;

import androidx.room.Entity;

@Entity(tableName = "groupRecipe", primaryKeys = {"recipeId", "groupId"})
public class GroupRecipe {
    private Long recipeId;
    private Long groupId;
}
