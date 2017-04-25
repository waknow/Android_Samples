package com.example.administrator.retrofit2;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.stackexchange.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        StackOverflowInterface so = retrofit.create(StackOverflowInterface.class);
        so.questions("android").enqueue(this);
        return v;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Item item = (Item) getListAdapter().getItem(position);
        ((Contract) getActivity()).onQuestion(item);
    }

    @Override
    public void onResponse(Call<SOQuestions> call, Response<SOQuestions> response) {
        setListAdapter(new ItemAdapter(response.body().items));
    }

    @Override
    public void onFailure(Call<SOQuestions> call, Throwable t) {
        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
        Log.e(getClass().getSimpleName(), "Exception from Retrofit request to StackOverflow", t);
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
