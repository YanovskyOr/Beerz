package ory.ofir.beerz.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.webkit.URLUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

public class Model {
    public static Model instance = new Model();
    ModelFirebase modelFirebase;
    private Model(){
        modelFirebase = new ModelFirebase();
    }

    public void cancelGetAllBeers() {
        modelFirebase.cancelGetAllBeers();
    }

    class BeerListData extends  MutableLiveData<List<Beer>>{

        @Override
        protected void onActive() {
            super.onActive();
            // new thread tasks
            // 1. get the beer list from the local DB
            BeerAsyncDao.getAll(new BeerAsyncDao.BeerAsyncDaoListener<List<Beer>>() {
                @Override
                public void onComplete(List<Beer> data) {
                    // 2. update the live data with the new beer list
                    setValue(data);
                    Log.d("TAG","got beers from local DB " + data.size());

                    // 3. get the beer list from firebase
                    modelFirebase.getAllBeers(new ModelFirebase.GetAllBeersListener() {
                        @Override
                        public void onSuccess(List<Beer> beerslist) {
                            // 4. update the live data with the new beer list
                            setValue(beerslist);
                            Log.d("TAG","got beers from firebase " + beerslist.size());

                            // 5. update the local DB
                            BeerAsyncDao.insertAll(beerslist, new BeerAsyncDao.BeerAsyncDaoListener<Boolean>() {
                                @Override
                                public void onComplete(Boolean data) {

                                }
                            });
                        }
                    });
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            modelFirebase.cancelGetAllBeers();
            Log.d("TAG","cancelGetAllBeers");
        }

        public BeerListData() {
            super();
            //setValue(AppLocalDb.db.beerDao().getAll());
            setValue(new LinkedList<Beer>());
        }
    }

    BeerListData beerListData = new BeerListData();

    public LiveData<List<Beer>> getAllBeers(){
        return beerListData;
    }

    public void addBeer(Beer br){
        modelFirebase.addBeer(br);
    }



    ////////////////////////////////////////////////////////
    //  Handle Image Files
    ////////////////////////////////////////////////////////


    public interface SaveImageListener{
        void onDone(String url);
    }

    public void saveImage(Bitmap imageBitmap, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap,listener);
    }

    public interface GetImageListener{
        void onDone(Bitmap imageBitmap);
    }

    public void getImage(final String url, final GetImageListener listener ){
        String localFileName = URLUtil.guessFileName(url, null, null);
        final Bitmap image = loadImageFromFile(localFileName);
        if (image == null) {                                      //if image not found - try downloading it from parse
            modelFirebase.getImage(url, new GetImageListener() {
                @Override
                public void onDone(Bitmap imageBitmap) {
                    if (imageBitmap == null) {
                        listener.onDone(null);
                    }else {
                        //2.  save the image locally
                        String localFileName = URLUtil.guessFileName(url, null, null);
                        Log.d("TAG", "save image to cache: " + localFileName);
                        saveImageToFile(imageBitmap, localFileName);
                        //3. return the image using the listener
                        listener.onDone(imageBitmap);
                    }
                }
            });
        }else {
            Log.d("TAG","OK reading cache image: " + localFileName);
            listener.onDone(image);
        }
    }

    // Store / Get from local mem
    private void saveImageToFile(Bitmap imageBitmap, String imageFileName){
        if (imageBitmap == null) return;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File imageFile = new File(dir,imageFileName);
            imageFile.createNewFile();

            OutputStream out = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

            //addPictureToGallery(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadImageFromFile(String imageFileName){
        Bitmap bitmap = null;
        try {
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(dir,imageFileName);
            InputStream inputStream = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("tag","got image from cache: " + imageFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
