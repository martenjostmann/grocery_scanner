package de.grocery_scanner;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.grocery_scanner.persistence.dao.EanDAO;
import de.grocery_scanner.persistence.dao.InventoryDAO;
import de.grocery_scanner.persistence.elements.Ean;
import de.grocery_scanner.persistence.elements.Inventory;
import de.grocery_scanner.persistence.dao.InventoryDAO.inventoryEan;

public class AppRepository {
    private EanDAO eanDAO;
    private InventoryDAO inventoryDAO;
    private LiveData<List<inventoryEan>> getInventory;
    private LiveData<Integer> inventoryQuantity;
    private LiveData<List<EanDAO.ItemsWithCount>> itemsWithCount;

    public AppRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        eanDAO = database.getEanDAO();
        inventoryDAO = database.getInventoryDAO();
        inventoryQuantity = inventoryDAO.inventoryQuantity();
        getInventory = inventoryDAO.getInventory();
        itemsWithCount = eanDAO.getItemsWithCount();
    }

    public void insert(Inventory... items) {
        new InsertInventoryAsyncTask(inventoryDAO).execute(items);
    }

    public void insert(Ean... eans) {
        new InsertEanAsyncTask(eanDAO).execute(eans);
    }

    public void update(Inventory... items) {
        new UpdateInventoryAsyncTask(inventoryDAO).execute(items);
    }

    public void update(Ean... eans) {
        new UpdateEanAsyncTask(eanDAO).execute(eans);
    }

    public void delete(Inventory item) {
        new DeleteInventoryAsyncTask(inventoryDAO).execute(item);
    }

    public void delete(Ean ean) {
        new DeleteEanAsyncTask(eanDAO).execute(ean);
    }

    public LiveData<List<inventoryEan>> getInventory() {
        return getInventory;
    }

    public Inventory getItembyId(int id) {
        try {
            return new getItemByIdAsyncTask(inventoryDAO).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Inventory getItemByEanId(String ean) {
        try {
            return new getItemByEanIdAsyncTask(inventoryDAO).execute(ean).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int checkInventory(String id) {
        try {
            return new checkInventoryAsyncTask(inventoryDAO).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int checkIfEmpty() {
        return inventoryDAO.checkIfEmpty();
    }

    public LiveData<List<inventoryEan>> getFavourite(int limit) {
        return inventoryDAO.getFavourite(limit);
    }

    public LiveData<Integer> getInventoryQuantity() {
        return inventoryQuantity;
    }

    public List<Ean> getAllEan() {
        return eanDAO.getAllEan();
    }

    public Ean getEanById(String id) {
        return eanDAO.getItemById(id);
    }

    public int checkEan(String id) {
        return eanDAO.checkEan(id);
    }

    public int checkIfEanEmpty() {
        return eanDAO.checkIfEanEmpty();
    }

    public LiveData<List<EanDAO.ItemsWithCount>> getItemsWithCount() {
        return eanDAO.getItemsWithCount();
    }

    private static class InsertInventoryAsyncTask extends AsyncTask<Inventory, Void, Void> {

        private InventoryDAO inventoryDAO;

        private  InsertInventoryAsyncTask(InventoryDAO inventoryDAO) {
            this.inventoryDAO = inventoryDAO;
        }

        @Override
        protected Void doInBackground(Inventory... inventories) {
            inventoryDAO.insert(inventories);
            return null;
        }
    }

    private static class InsertEanAsyncTask extends AsyncTask<Ean, Void, Void> {

        private EanDAO eanDAO;

        private  InsertEanAsyncTask(EanDAO eanDAO) {
            this.eanDAO = eanDAO;
        }

        @Override
        protected Void doInBackground(Ean... eans) {
            eanDAO.insert(eans);
            return null;
        }
    }

    private static class UpdateInventoryAsyncTask extends AsyncTask<Inventory, Void, Void> {

        private InventoryDAO inventoryDAO;

        private UpdateInventoryAsyncTask(InventoryDAO inventoryDAO) {
            this.inventoryDAO = inventoryDAO;
        }

        @Override
        protected Void doInBackground(Inventory... inventories) {
            inventoryDAO.update(inventories);
            return null;
        }

    }

    private static class UpdateEanAsyncTask extends AsyncTask<Ean, Void, Void> {

        private EanDAO eanDAO;

        private UpdateEanAsyncTask(EanDAO eanDAO) {
            this.eanDAO = eanDAO;
        }

        @Override
        protected Void doInBackground(Ean... eans) {
            eanDAO.update(eans);
            return null;
        }
    }

    private static class DeleteInventoryAsyncTask extends AsyncTask<Inventory, Void, Void> {

        private InventoryDAO inventoryDAO;

        private DeleteInventoryAsyncTask(InventoryDAO inventoryDAO) {
            this.inventoryDAO = inventoryDAO;
        }

        @Override
        protected Void doInBackground(Inventory... inventories) {
            inventoryDAO.delete(inventories[0]);
            return null;
        }
    }

    private static class DeleteEanAsyncTask extends AsyncTask<Ean, Void, Void> {

        private EanDAO eanDAO;

        private DeleteEanAsyncTask(EanDAO eanDAO) {
            this.eanDAO = eanDAO;
        }

        @Override
        protected Void doInBackground(Ean... eans) {
            eanDAO.delete(eans[0]);
            return null;
        }
    }

    private static class getItemByIdAsyncTask extends AsyncTask<Integer, Void, Inventory> {

        private InventoryDAO inventoryDAO;

        private getItemByIdAsyncTask(InventoryDAO inventoryDAO) {
            this.inventoryDAO = inventoryDAO;
        }

        @Override
        protected Inventory doInBackground(Integer... items) {
            return inventoryDAO.getItemById(items[0]);
        }

        @Override
        protected void onPostExecute(Inventory inventory) {
            super.onPostExecute(inventory);
        }
    }



    private static class getItemByEanIdAsyncTask extends AsyncTask<String, Void, Inventory> {

        private InventoryDAO inventoryDAO;

        private getItemByEanIdAsyncTask(InventoryDAO inventoryDAO) {
            this.inventoryDAO = inventoryDAO;
        }

        @Override
        protected Inventory doInBackground(String... ean) {
            return inventoryDAO.getItemByEanId(ean[0]);
        }

        @Override
        protected void onPostExecute(Inventory inventory) {
            super.onPostExecute(inventory);
        }
    }

    private static class checkInventoryAsyncTask extends AsyncTask<String, Void, Integer> {

        private InventoryDAO inventoryDAO;

        private checkInventoryAsyncTask(InventoryDAO inventoryDAO) {
            this.inventoryDAO = inventoryDAO;
        }

        @Override
        protected Integer doInBackground(String... id) {
            return inventoryDAO.checkInventory(id[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }
    }

}
