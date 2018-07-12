package ory.ofir.beerz.Model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import ory.ofir.beerz.MainActivity;
import ory.ofir.beerz.MyApplication;

@Database(entities = {Beer.class}, version = 1)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract BeerDao beerDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db = Room.databaseBuilder(MainActivity.contextCompat,
            AppLocalDbRepository.class,
            "dbFileName.db").fallbackToDestructiveMigration().build();
}
