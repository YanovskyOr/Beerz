package ory.ofir.beerz;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;


import java.util.List;

import ory.ofir.beerz.Model.Beer;
import ory.ofir.beerz.Model.Model;

public class BeersListFragment extends Fragment {
    ListView list;
    MyAdapter myAdapter = new MyAdapter();;
    BeersListViewModel dataModel;

    public static BeersListFragment newInstance() {
        BeersListFragment fragment = new BeersListFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataModel = ViewModelProviders.of(this).get(BeersListViewModel.class);
        dataModel.getData().observe(this, new Observer<List<Beer>>() {
            @Override
            public void onChanged(@Nullable List<Beer> beers) {
                myAdapter.notifyDataSetChanged();
                Log.d("TAG","notifyDataSetChanged" + beers.size());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Model.instance.cancelGetAllBeers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_beers_list, container, false);

        list = view.findViewById(R.id.beerslist_list);
        list.setAdapter(myAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG","item selected:" + i);
            }
        });
        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }


    class MyAdapter extends BaseAdapter {
        public MyAdapter(){
        }

        @Override
        public int getCount() {
            Log.d("TAG","list size:" + dataModel.getData().getValue().size());

            return dataModel.getData().getValue().size();

        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null){
                view = LayoutInflater.from(getActivity()).inflate(R.layout.beer_list_item,null);
            }

            final Beer br = dataModel.getData().getValue().get(i);

            TextView nameTv = view.findViewById(R.id.br_name_tv);
            TextView ratingTv = view.findViewById(R.id.br_rating_tv);
            final ImageView avatarView = view.findViewById(R.id.br_picture_tv);

            nameTv.setText(br.name);
            ratingTv.setText(Float.toString(br.rating));
            // check following line
            //avatarView.setImageResource(R.drawable.avatar;
            avatarView.setImageResource(R.drawable.common_google_signin_btn_icon_dark);

            avatarView.setTag(br.id);
            if (br.picture != null){
                Model.instance.getImage(br.picture, new Model.GetImageListener() {
                    @Override
                    public void onDone(Bitmap imageBitmap) {
                        if (br.id.equals(avatarView.getTag()) && imageBitmap != null) {
                            avatarView.setImageBitmap(imageBitmap);
                        }
                    }
                });
            }
            return view;
        }
    }
}
