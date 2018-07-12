package ory.ofir.beerz.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface BeerDao {
    @Query("select * from Beer ORDER BY id DESC")
    List<Beer> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Beer... beers);

    @Delete
    void delete(Beer beer);
}
