package de.grocery_scanner;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import de.grocery_scanner.persistence.dao.eanDAO;
import de.grocery_scanner.persistence.dao.inventoryDAO;
import de.grocery_scanner.persistence.elements.inventory;
import de.grocery_scanner.persistence.dao.inventoryDAO.inventoryEan;

public class AppRepository {
    private eanDAO eanDAO;
    private inventoryDAO inventoryDAO;
    private LiveData<List<inventoryEan>> getInventory;
    private LiveData<Integer> inventoryQuantity;

    public AppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        eanDAO = database.getEanDAO();
        inventoryDAO = database.getInventoryDAO();
        inventoryQuantity = inventoryDAO.inventoryQuantity();
        getInventory = inventoryDAO.getInventory();
    }

    public void insert(inventory... items) {
        new InsertInventoryAsyncTask(inventoryDAO).execute(items);
    }

    public void update(inventory... items) {
        new UpdateInventoryAsyncTask(inventoryDAO).execute(items);
    }

    public void delete(inventory item) {
        new DeleteInventoryAsyncTask(inventoryDAO).execute(item);
    }

    public LiveData<List<inventoryEan>> getInventory() {
        return getInventory;
    }

    public inventory getItemById(int id) {
        return inventoryDAO.getItemById(id);
    }

    public inventory getItemByEanId(String ean) {
        return inventoryDAO.getItemByEanId(ean);
    }

    public int checkInventory(String id) {
        return inventoryDAO.checkInventory(id);
    }

    public int checkIfEmpty() {
        return  inventoryDAO.checkIfEmpty();
    }

    public LiveData<List<inventoryEan>> getFavourite(int limit) {
        return inventoryDAO.getFavourite(limit);
    }

    public LiveData<Integer> getInventoryQuantity() {
        return inventoryQuantity;
    }

    private static class InsertInventoryAsyncTask extends AsyncTask<inventory, Void, Void> {

        private inventoryDAO inventoryDAO;

        private  InsertInventoryAsyncTask(inventoryDAO inventoryDAO) {
            this.inventoryDAO = inventoryDAO;
        }

        @Override
        protected Void doInBackground(inventory... inventories) {
            inventoryDAO.insert(inventories);
            return null;
        }
    }

    private static class UpdateInventoryAsyncTask extends AsyncTask<inventory, Void, Void> {

        private inventoryDAO inventoryDAO;

        private UpdateInventoryAsyncTask(inventoryDAO inventoryDAO) {
            this.inventoryDAO = inventoryDAO;
        }

        @Override
        protected Void doInBackground(inventory... inventories) {
            inventoryDAO.update(inventories);
            return null;
        }
    }

    private static class DeleteInventoryAsyncTask extends AsyncTask<inventory, Void, Void> {

        private inventoryDAO inventoryDAO;

        private DeleteInventoryAsyncTask(inventoryDAO inventoryDAO) {
            this.inventoryDAO = inventoryDAO;
        }

        @Override
        protected Void doInBackground(inventory... inventories) {
            inventoryDAO.delete(inventories[0]);
            return null;
        }
    }

    private static class getItemByEanIdAsyncTask extends AsyncTask<String, Void, inventory> {

        private inventoryDAO inventoryDAO;

        private getItemByEanIdAsyncTask(inventoryDAO inventoryDAO) {
            this.inventoryDAO = inventoryDAO;
        }

        @Override
        protected inventory doInBackground(String... ean) {
            return inventoryDAO.getItemByEanId(ean[0]);
        }
    }

}
