package de.grocery_scanner.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import de.grocery_scanner.AppRepository;
import de.grocery_scanner.persistence.elements.ArticleGroup;
import de.grocery_scanner.persistence.elements.Recipe;

public class ArticleGroupViewModel extends AndroidViewModel {

    private AppRepository repository;

    public ArticleGroupViewModel(@NonNull Application application) {
        super(application);

        repository = new AppRepository(application);
    }

    public void insert(ArticleGroup... items) {
        repository.insert(items);
    }

    public void update(ArticleGroup... items) {
        repository.update(items);
    }

    public void delete(ArticleGroup item) {
        repository.delete(item);
    }

    public LiveData<List<ArticleGroup>> getAllArticleGroups() {
        return repository.getAllArticleGroups();
    }
}
