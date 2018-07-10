package ory.ofir.beerz.Model;

import android.os.AsyncTask;
import java.util.List;


public class BeerAsyncDao {

    interface BeerAsyncDaoListener<T>{
        void onComplete(T data);
    }
    static public void getAll(final BeerAsyncDaoListener<List<Beer>> listener) {
        class MyAsynchTask extends AsyncTask<String,String,List<Beer>>{
            @Override
            protected List<Beer> doInBackground(String... strings) {
                List<Beer> stList = AppLocalDb.db.beerDao().getAll();
                return stList;
            }

            @Override
            protected void onPostExecute(List<Beer> beers) {
                super.onPostExecute(beers);
                listener.onComplete(beers);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute();
    }


    static void insertAll(final List<Beer> beers, final BeerAsyncDaoListener<Boolean> listener){
        class MyAsynchTask extends AsyncTask<List<Beer>,String,Boolean>{
            @Override
            protected Boolean doInBackground(List<Beer>... beers) {
                for (Beer br:beers[0]) {
                    AppLocalDb.db.beerDao().insertAll(br);
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                listener.onComplete(success);
            }
        }
        MyAsynchTask task = new MyAsynchTask();
        task.execute(beers);
    }
}
