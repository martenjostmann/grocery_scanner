package de.grocery_scanner.helper.insertInventory;

import java.util.Date;

import de.grocery_scanner.AppDatabase;
import de.grocery_scanner.persistence.elements.inventory;

public class insertInventory {

    private de.grocery_scanner.persistence.dao.inventoryDAO inventoryDAO;

    public insertInventory(AppDatabase database, String barCode) {
        inventoryDAO = database.getInventoryDAO();
        inventory inventoryEle = new inventory();
        inventoryEle.setEanId(barCode);
        inventoryEle.setInDate(new Date());
        inventoryDAO.insert(inventoryEle);
    }

}
