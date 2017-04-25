package com.example.administrator.picasso;

import android.app.ListFragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Administrator on 2017/4/25.
 */

public class QuestionsFragment extends ListFragment implements Callback<SOQuestions> {

    public interface Contract {
        void onQuestion(Item question);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://api.stackexchange.com").build();
        StackOverflowInterface so = restAdapter.create(StackOverflowInterface.class);
        so.questions("android", this);

        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Item item = (Item) getListAdapter().getItem(position);
        ((Contract) getActivity()).onQuestion(item);
    }

    @Override
    public void success(SOQuestions soQuestions, Response response) {
        setListAdapter(new ItemAdapter(soQuestions.items));
    }

    @Override
    public void failure(RetrofitError error) {
        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
        Log.e(getClass().getSimpleName(), "Exception from Retrofit request to StackOverflow", error);
    }


    class ItemAdapter extends ArrayAdapter<Item> {
        ItemAdapter(List<Item> items) {
            super(getActivity(), R.layout.row, R.id.title, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            Item item = getItem(position);
            ImageView icon = (ImageView) view.findViewById(R.id.icon);
            Picasso.with(getActivity()).load(item.owner.profileImage)
                    .fit().centerCrop()
                    .placeholder(R.drawable.owner_placeholder)
                    .error(R.drawable.owner_error)
                    .into(icon);
            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(Html.fromHtml(getItem(position).title));

            return view;
        }
    }
}
