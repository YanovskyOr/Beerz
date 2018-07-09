package ory.ofir.beerz;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import ory.ofir.beerz.Model.Beer;
import ory.ofir.beerz.Model.Model;

public class BeersListViewModel extends ViewModel {
    LiveData<List<Beer>> data;

    public LiveData<List<Beer>> getData(){
        data = Model.instance.getAllBeers();
        return data;
    }
}
