package de.grocery_scanner.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import de.grocery_scanner.AppRepository;
import de.grocery_scanner.persistence.elements.ean;

public class EanViewModel extends AndroidViewModel {
    private AppRepository repository;

    public EanViewModel(@NonNull Application application) {
        super(application);
        repository = new AppRepository(application);
    }

    public void insert(ean... eans) {
        repository.insert(eans);
    }

    public void update(ean... eans) {
        repository.update(eans);
    }

    public void delete(ean ean) {
        repository.delete(ean);
    }

    public List<ean> getAllEan() {
        return repository.getAllEan();
    }

    public ean getItemById(String id) {
        return repository.getEanById(id);
    }

    public int checkIfEanEmpty() {
        return repository.checkIfEanEmpty();
    }

    public int checkEan(String id) {
        return repository.checkEan(id);
    }
}
