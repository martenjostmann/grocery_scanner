package de.grocery_scanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import de.grocery_scanner.AppRepository;
import de.grocery_scanner.persistence.elements.Ean;

public class EanViewModel extends AndroidViewModel {
    private AppRepository repository;

    public EanViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
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
}
