package de.grocery_scanner.persistence.elements;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "ean",
        foreignKeys = @ForeignKey(entity = ArticleGroup.class, parentColumns = "groupId", childColumns = "groupId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class Ean {
    @PrimaryKey()
    @NonNull
    private String eanId;

    @NonNull
    private Long groupId;

    @NonNull
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(@NonNull Long groupId) {
        this.groupId = groupId;
    }

    @NonNull
    private String name;

    public void setEanId(@NonNull String eanId) {
        this.eanId = eanId;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getEanId() {
        return eanId;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
