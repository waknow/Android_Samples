package com.example.administrator.retrofit;

import android.app.ListFragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
            super(getActivity(), android.R.layout.simple_list_item_1, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView title = (TextView) view.findViewById(android.R.id.text1);
            title.setText(Html.fromHtml(getItem(position).title));

            return view;
        }
    }
}
