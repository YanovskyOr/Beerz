package ory.ofir.beerz.View;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.view.View.MeasureSpec;

import java.util.ArrayList;
import java.util.List;

import ory.ofir.beerz.BeersListViewModel;
import ory.ofir.beerz.Model.Beer;
import ory.ofir.beerz.Model.Model;
import ory.ofir.beerz.R;

public class BeerDetailsFragment extends BottomSheetDialogFragment {

    ViewModel beerDetailsViewModel;

    public String id;
    public String name;
    public String image;
    public String desc;
    public float rating;
    public ArrayList<String> comments;

    private TextView mBeerName;
    private ImageView mBeerImage;
    private TextView mBeerDesc;
    private RatingBar mRatingBar;
    private ListView mCommentsList;

    ArrayAdapter arrayAdapter;

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
        TextView mBeerDesc = view.findViewById(R.id.beer_desc_tv);
        RatingBar mRatingBar = view.findViewById(R.id.beer_rating_rb);
        mCommentsList = view.findViewById(R.id.comments_list);
        final TextView mNewComment = view.findViewById(R.id.beer_details_add_comment_et);
        Button mSendComment = view.findViewById(R.id.send_comment_btn);

        mBeerName.setText(name);
        mBeerDesc.setText(desc);
        mRatingBar.setRating(rating);

        if(comments != null) {
            arrayAdapter = new ArrayAdapter(getActivity(), R.layout.comment_list_item,R.id.comment_tv, comments);
            mCommentsList.setAdapter(arrayAdapter);
        }

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
        getListItemsHeight();

        mSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG",mNewComment.getText().toString());
                comments.add(mNewComment.getText().toString());
                mCommentsList.getAdapter().notify();

            }
        });

        return view;
    }

    public void getListItemsHeight(){
        int totalHeight = 0;
        for (int i = 0; i < mCommentsList.getCount(); i++) {
            View item = arrayAdapter.getView(i, null, mCommentsList);
            item.measure(MeasureSpec.makeMeasureSpec(mCommentsList.getWidth(), MeasureSpec.AT_MOST),0);
            totalHeight += item.getMeasuredHeight();
            Log.d("TAG","item height:" + item.getMeasuredHeight());
        }

        Log.d("TAG","total height:" + totalHeight);
        ViewGroup.LayoutParams params = mCommentsList.getLayoutParams();
        params.height = totalHeight + (mCommentsList.getDividerHeight() * (mCommentsList.getCount() - 1));
        mCommentsList.setLayoutParams(params);
        mCommentsList.requestLayout();
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        beerDetailsViewModel = ViewModelProviders.of(this).get(BeersListViewModel.class);
//        beerDetailsViewModel.Model.instance.getAllBeers();().observe(this, new Observer<ArrayList<String>>() {
//            @Override
//            public void onChanged(@Nullable ArrayList<String> comments) {
//            arrayAdapter.notifyDataSetChanged();
//            }
//        });
//    }
}
