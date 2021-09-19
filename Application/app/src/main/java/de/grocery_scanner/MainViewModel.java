package de.grocery_scanner;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.grocery_scanner.AppRepository;
import de.grocery_scanner.persistence.dao.inventoryDAO;
import de.grocery_scanner.persistence.elements.inventory;

public class MainViewModel extends AndroidViewModel {

    private AppRepository repository;
    private LiveData<List<inventoryDAO.inventoryEan>> getInventory;
    private LiveData<Integer> inventoryQuantity;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        getInventory = repository.getInventory();
        inventoryQuantity = repository.getInventoryQuantity();
    }

    public void insert(inventory... items) {
        repository.insert(items);
    }

    public void update(inventory... items) {
        repository.update(items);
    }

    public void delete(inventory item) {
        repository.delete(item);
    }

    public LiveData<Integer> getInventoryQuantity() {
        return inventoryQuantity;
    }

    public LiveData<List<inventoryDAO.inventoryEan>> getInventory() {
        return getInventory;
    }

    public inventory getItemById(int id) {
        return repository.getItemById(id);
    }

    public inventory getItemByEanId(String ean) {
        return repository.getItemByEanId(ean);
    }

    public int checkInventory(String id) {
        return repository.checkInventory(id);
    }

    public int checkIfEmpty() {
        return repository.checkIfEmpty();
    }

    public LiveData<List<inventoryDAO.inventoryEan>> getFavourite(int limit) {
        return repository.getFavourite(limit);
    }
}
