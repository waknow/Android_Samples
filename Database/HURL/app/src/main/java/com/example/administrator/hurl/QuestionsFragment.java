package com.example.administrator.hurl;

import android.app.ListFragment;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */

public class QuestionsFragment extends ListFragment {
    public interface Contract {
        void onQuestion(Item question);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        EventBus.getDefault().register(this);
        new LoadThread().start();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Item item = (Item) getListAdapter().getItem(position);
        ((Contract) getActivity()).onQuestion(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onQuestionsLoaded(QuestionsLoadedEvent event) {
        setListAdapter(new ItemAdapter(event.getQuestions().items));
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
