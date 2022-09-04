package de.grocery_scanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.grocery_scanner.AppRepository;
import de.grocery_scanner.persistence.dao.EanDAO;
import de.grocery_scanner.persistence.elements.Ean;

public class EanViewModel extends AndroidViewModel {
    private AppRepository repository;

    private LiveData<List<EanDAO.ItemsWithCount>> itemsWithCount;

    public EanViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);

        itemsWithCount = repository.getItemsWithCount();
    }

    public void insert(Ean... eans) {
        repository.insert(eans);
    }

    public void update(Ean... eans) {
        repository.update(eans);
    }

    public void delete(Ean ean) {
        repository.delete(ean);
    }

    public List<Ean> getAllEan() {
        return repository.getAllEan();
    }

    public Ean getItemById(String id) {
        return repository.getEanById(id);
    }

    public int checkIfEanEmpty() {
        return repository.checkIfEanEmpty();
    }

    public int checkEan(String id) {
        return repository.checkEan(id);
    }

    public LiveData<List<EanDAO.ItemsWithCount>> getItemsWithCount() {
        return itemsWithCount;
    }

    public LiveData<List<EanDAO.ItemsWithCount>> getItemsWithCountSearch(String name) {
        return repository.getItemsWithCountSearch(name);
    }
}
