package de.grocery_scanner;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import de.grocery_scanner.persistence.dao.eanDAO;
import de.grocery_scanner.persistence.dao.inventoryDAO;
import de.grocery_scanner.persistence.elements.ean;
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

    public void insert(ean... eans) {
        new InsertEanAsyncTask(eanDAO).execute(eans);
    }

    public void update(inventory... items) {
        new UpdateInventoryAsyncTask(inventoryDAO).execute(items);
    }

    public void update(ean... eans) {
        new UpdateEanAsyncTask(eanDAO).execute(eans);
    }

    public void delete(inventory item) {
        new DeleteInventoryAsyncTask(inventoryDAO).execute(item);
    }

    public void delete(ean ean) {
        new DeleteEanAsyncTask(eanDAO).execute(ean);
    }

    public LiveData<List<inventoryEan>> getInventory() {
        return getInventory;
    }

    public inventory getItembyId(int id) {
        try {
            return new getItemByIdAsyncTask(inventoryDAO).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public inventory getItemByEanId(String ean) {
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

    public List<ean> getAllEan() {
        return eanDAO.getAllEan();
    }

    public ean getEanById(String id) {
        return eanDAO.getItemById(id);
    }

    public int checkEan(String id) {
        return eanDAO.checkEan(id);
    }

    public int checkIfEanEmpty() {
        return eanDAO.checkIfEanEmpty();
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

    private static class InsertEanAsyncTask extends AsyncTask<ean, Void, Void> {

        private eanDAO eanDAO;

        private  InsertEanAsyncTask(eanDAO eanDAO) {
            this.eanDAO = eanDAO;
        }

        @Override
        protected Void doInBackground(ean... eans) {
            eanDAO.insert(eans);
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

    private static class UpdateEanAsyncTask extends AsyncTask<ean, Void, Void> {

        private eanDAO eanDAO;

        private UpdateEanAsyncTask(eanDAO eanDAO) {
            this.eanDAO = eanDAO;
        }

        @Override
        protected Void doInBackground(ean... eans) {
            eanDAO.update(eans);
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

    private static class DeleteEanAsyncTask extends AsyncTask<ean, Void, Void> {

        private eanDAO eanDAO;

        private DeleteEanAsyncTask(eanDAO eanDAO) {
            this.eanDAO = eanDAO;
        }

        @Override
        protected Void doInBackground(ean... eans) {
            eanDAO.delete(eans[0]);
            return null;
        }
    }

    private static class getItemByIdAsyncTask extends AsyncTask<Integer, Void, inventory> {

        private inventoryDAO inventoryDAO;

        private getItemByIdAsyncTask(inventoryDAO inventoryDAO) {
            this.inventoryDAO = inventoryDAO;
        }

        @Override
        protected inventory doInBackground(Integer... items) {
            return inventoryDAO.getItemById(items[0]);
        }

        @Override
        protected void onPostExecute(inventory inventory) {
            super.onPostExecute(inventory);
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

        @Override
        protected void onPostExecute(inventory inventory) {
            super.onPostExecute(inventory);
        }
    }

    private static class checkInventoryAsyncTask extends AsyncTask<String, Void, Integer> {

        private inventoryDAO inventoryDAO;

        private checkInventoryAsyncTask(inventoryDAO inventoryDAO) {
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
