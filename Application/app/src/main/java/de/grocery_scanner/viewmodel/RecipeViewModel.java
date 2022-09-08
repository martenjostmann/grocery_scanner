package de.grocery_scanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import de.grocery_scanner.AppRepository;
import de.grocery_scanner.persistence.elements.Ean;
import de.grocery_scanner.persistence.elements.Recipe;

public class RecipeViewModel extends AndroidViewModel {

    private AppRepository repository;

    public RecipeViewModel(@NonNull Application application) {
        super(application);

        repository = new AppRepository(application);
    }

    public void insert(Recipe... items) {
        repository.insert(items);
    }

    public void update(Recipe... items) {
        repository.update(items);
    }

    public void delete(Recipe item) {
        repository.delete(item);
    }

}
