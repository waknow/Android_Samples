package com.example.administrator.eu4you;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/5/3.
 */

public class CountriesFragment extends ContractListFragment<CountriesFragment.Contract> {

    interface Contract {
        void onCountrySelected(Country c);

        boolean isPersistentSelection();
    }

    private static final String STATE_CHECKED = "com.example.administrator.eu4you.STATE_CHECKED";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new CountryAdapter());

        if (savedInstanceState != null) {
            int position = savedInstanceState.getInt(STATE_CHECKED, -1);
            if (position > -1) {
                getListView().setItemChecked(position, true);
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (getContract().isPersistentSelection()) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            l.setItemChecked(position, true);
        } else {
            getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        }
        getContract().onCountrySelected(Country.EU.get(position));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_CHECKED, getListView().getCheckedItemPosition());
    }

    class CountryAdapter extends ArrayAdapter<Country> {
        CountryAdapter() {
            super(getActivity(), R.layout.row, R.id.name, Country.EU);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CountryViewHolder wrapper;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.row, parent, false);
                wrapper = new CountryViewHolder(convertView);
                convertView.setTag(wrapper);
            } else {
                wrapper = (CountryViewHolder) convertView.getTag();
            }
            wrapper.populateFrom(getItem(position));
            return convertView;
        }
    }
}
