package ory.ofir.beerz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.ActionBar;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.graphics.Bitmap;
import android.widget.RatingBar;
import android.widget.TextView;

import ory.ofir.beerz.Model.Beer;
import ory.ofir.beerz.Model.Model;

import static android.app.Activity.RESULT_OK;


public class AddBeerFragment extends Fragment {
    private static final String ARG_NAME = "ARG_NAME";
    private static final String ARG_ID = "ARG_ID";

    public AddBeerFragment() {

    }


    EditText beerNameTe;
    EditText beerReviewTe;
    ImageView beerPicIv;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ProgressBar progress;
    RatingBar ratingBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setElevation(0);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.colorBg));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_beer, container, false);

        beerNameTe = view.findViewById(R.id.add_beer_name_te);
        beerNameTe.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        beerReviewTe = view.findViewById(R.id.add_beer_review_te);
        ratingBar = (RatingBar) view.findViewById(R.id.add_beer_rating);
        progress = view.findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);

        Button save = view.findViewById(R.id.add_beer_add_btn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);

                final Beer beer = new Beer();
                beer.name = beerNameTe.getText().toString();
                beer.id = "2";
                beer.description = beerReviewTe.getText().toString();
                beer.rating = ratingBar.getRating();

                //save image
                if (imageBitmap != null) {
                    Model.instance.saveImage(imageBitmap, new Model.SaveImageListener() {
                        @Override
                        public void onDone(String url) {
                            //save beer object
                            beer.picture = url;
                            Model.instance.addBeer(beer);
                            getActivity().finish();
                        }
                    });
                }
            }
        });

        Button cancel = view.findViewById(R.id.add_beer_cancel_btn);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(ARG_NAME);
            if (name != null) {
                beerNameTe.setText(name);
            }
//            String id = savedInstanceState.getString(ARG_ID);
//            if (id != null) {
//                beerId.setText(id);
//            }
        }
        beerPicIv = view.findViewById(R.id.add_beer_pic_iv);
        beerPicIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open camera
                Intent takePictureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
        beerPicIv = view.findViewById(R.id.add_beer_pic_iv);
        return view;

    }

    Bitmap imageBitmap;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            beerPicIv.setImageBitmap(imageBitmap);
        }
    }


        @Override
        public void onDestroyView(){
            super.onDestroyView();
        }
        @Override
        public void  onSaveInstanceState(Bundle bundle){
            super.onSaveInstanceState(bundle);
            bundle.putString(ARG_NAME, beerNameTe.getText().toString());
//            bundle.putString(ARG_ID, idEt.getText().toString());
        }
}
