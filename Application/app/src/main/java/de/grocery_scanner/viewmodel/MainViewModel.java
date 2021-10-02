package de.grocery_scanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import de.grocery_scanner.AppRepository;
import de.grocery_scanner.persistence.dao.InventoryDAO;
import de.grocery_scanner.persistence.elements.Inventory;

public class MainViewModel extends AndroidViewModel {

    private AppRepository repository;
    private LiveData<List<InventoryDAO.inventoryEan>> getInventory;
    private LiveData<Integer> inventoryQuantity;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
        getInventory = repository.getInventory();
        inventoryQuantity = repository.getInventoryQuantity();
    }

    public void insert(Inventory... items) {
        repository.insert(items);
    }

    public void insertInventorybyEan(String barCode){
        Inventory inventoryItem = new Inventory();
        inventoryItem.setEanId(barCode);
        inventoryItem.setInDate(new Date());
        this.insert(inventoryItem);
    }

    public void update(Inventory... items) {
        repository.update(items);
    }

    public void delete(Inventory item) {
        repository.delete(item);
    }

    public LiveData<Integer> getInventoryQuantity() {
        return inventoryQuantity;
    }

    public LiveData<List<InventoryDAO.inventoryEan>> getInventory() {
        return getInventory;
    }

    public Inventory getItemById(int id){
        return repository.getItembyId(id);
    }

    public Inventory getItemByEanId(String ean) {
        return repository.getItemByEanId(ean);
    }

    public int checkInventory(String id) {
        return repository.checkInventory(id);
    }

    public int checkIfEmpty() {
        return repository.checkIfEmpty();
    }

    public LiveData<List<InventoryDAO.inventoryEan>> getFavourite(int limit) {
        return repository.getFavourite(limit);
    }
}
