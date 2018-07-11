package ory.ofir.beerz.Model;

import android.os.AsyncTask;
import java.util.List;


public class BeerAsyncDao {

    interface BeerAsyncDaoListener<T>{
        void onComplete(T data);
    }
    static public void getAll(final BeerAsyncDaoListener<List<Beer>> listener) {
        class MyAsyncTask extends AsyncTask<String,String,List<Beer>>{
            @Override
            protected List<Beer> doInBackground(String... strings) {
                List<Beer> brList = AppLocalDb.db.beerDao().getAll();
                return brList;
            }

            @Override
            protected void onPostExecute(List<Beer> beers) {
                super.onPostExecute(beers);
                listener.onComplete(beers);
            }
        }
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }


    static void insertAll(final List<Beer> beers, final BeerAsyncDaoListener<Boolean> listener){
        class MyAsyncTask extends AsyncTask<List<Beer>,String,Boolean>{
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
        MyAsyncTask task = new MyAsyncTask();
        task.execute(beers);
    }
}
