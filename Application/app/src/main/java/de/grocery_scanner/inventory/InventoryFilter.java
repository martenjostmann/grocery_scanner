package de.grocery_scanner.inventory;

import de.grocery_scanner.inventory.filter.Group;
import de.grocery_scanner.inventory.filter.Sort;

public class InventoryFilter {
    private Sort sort;
    private Group group;

    public InventoryFilter(Sort sort, Group group) {
        this.sort = sort;
        this.group = group;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
