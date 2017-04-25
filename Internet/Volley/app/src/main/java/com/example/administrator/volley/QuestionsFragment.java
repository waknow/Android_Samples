package com.example.administrator.volley;

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

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;


/**
 * Created by Administrator on 2017/4/25.
 */

public class QuestionsFragment extends ListFragment implements Response.ErrorListener, Response.Listener<SOQuestions> {

    public interface Contract {
        void onQuestion(Item question);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GsonRequest<SOQuestions> request = new GsonRequest<>(getString(R.string.url),
                SOQuestions.class, null, this, this);
        VolleyManager.get(getActivity()).enqueue(request);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Item item = (Item) getListAdapter().getItem(position);
        ((Contract) getActivity()).onQuestion(item);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
        Log.e(getClass().getSimpleName(), "Exception from Retrofit request to StackOverflow", error);
    }

    @Override
    public void onResponse(SOQuestions response) {
        setListAdapter(new ItemAdapter(response.items));
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

            VolleyManager
                    .get(getActivity())
                    .loadImage(item.owner.profileImage, icon,
                            R.drawable.owner_placeholder,
                            R.drawable.owner_error);

            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(Html.fromHtml(getItem(position).title));

            return view;
        }
    }
}
