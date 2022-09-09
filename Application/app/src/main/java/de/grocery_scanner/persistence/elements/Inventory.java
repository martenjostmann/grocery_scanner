package de.grocery_scanner.persistence.elements;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "inventory",
        foreignKeys = @ForeignKey(entity = ArticleGroup.class, parentColumns = "groupId", childColumns = "groupId", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE))
public class Inventory {



    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int inventoryId;

    @NonNull
    private Long groupId;

    @NonNull
    private Date inDate;

    @Nullable
    private Date outDate;

    @ColumnInfo(defaultValue = "0")
    private int use;

    @Nullable
    private int allocationTime;

    @NonNull
    public int getInventoryId() {
        return inventoryId;
    }

    @NonNull
    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(@NonNull Long groupId) {
        this.groupId = groupId;
    }

    @NonNull
    public Date getInDate() {
        return inDate;
    }

    @Nullable
    public Date getOutDate() {
        return outDate;
    }

    public int getUse() {
        return use;
    }

    public int getAllocationTime() {
        return allocationTime;
    }

    public void setInventoryId(@NonNull int inventoryId) {
        this.inventoryId = inventoryId;
    }


    public void setInDate(@NonNull Date inDate) {
        this.inDate = inDate;
    }

    public void setOutDate(@Nullable Date outDate) {
        this.outDate = outDate;
    }

    public void setUse(int use) {
        this.use = use;
    }

    public void setAllocationTime(int allocationTime) {
        this.allocationTime = allocationTime;
    }
}
