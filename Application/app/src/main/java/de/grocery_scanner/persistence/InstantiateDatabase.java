package de.grocery_scanner.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import de.grocery_scanner.AppDatabase;


public class InstantiateDatabase {
    public AppDatabase getDatabase(Context context){
        return Room.databaseBuilder(context, AppDatabase .class, "grocery_database")
                .allowMainThreadQueries()
                .build();

    }

}