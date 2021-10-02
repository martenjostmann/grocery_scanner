package de.grocery_scanner.persistence.elements;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ean")
public class Ean {
    @PrimaryKey()
    @NonNull
    private String eanId;

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
