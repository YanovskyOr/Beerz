package ory.ofir.beerz.View;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import ory.ofir.beerz.Model.Model;
import ory.ofir.beerz.R;

public class BeerDetailsFragment extends Fragment {

    public String id;
    public String name;
    public String image;
    public String desc;
    public float rating;

    private TextView mBeerName;
    private ImageView mBeerImage;
    private TextView mBeerDesc;
    private RatingBar mRatingBar;

    public static BeerDetailsFragment newInstance() {
        BeerDetailsFragment fragment = new BeerDetailsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beer_details, container, false);

        mBeerName = view.findViewById(R.id.beer_name_tv);
        final ImageView mBeerImage = view.findViewById(R.id.beer_picture_tv);
        TextView mBeerDesc = view.findViewById(R.id.beerr_desc_tv);
        RatingBar mRatingBar = view.findViewById(R.id.beer_rating_rb);

        mBeerName.setText(name);
        mBeerDesc.setText(desc);
        mRatingBar.setRating(rating);

        mBeerImage.setTag(id);
        if (image != null) {
            Model.instance.getImage(image, new Model.GetImageListener() {
                @Override
                public void onDone(Bitmap imageBitmap) {
                    if (id.equals(mBeerImage.getTag()) && imageBitmap != null) {
                        int dimension = Math.min(imageBitmap.getWidth(), imageBitmap.getHeight());
                        imageBitmap = ThumbnailUtils.extractThumbnail(imageBitmap, dimension, dimension);
                        RoundedBitmapDrawable roundDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
                        roundDrawable.setCircular(true);
                        mBeerImage.setImageDrawable(roundDrawable);
                        //avatarView.setImageBitmap(imageBitmap);
                    }
                }
            });
        }

        return view;
    }
}
