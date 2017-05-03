package com.example.administrator.eu4you;

import android.app.ListFragment;
import android.content.Context;

/**
 * Created by Administrator on 2017/5/3.
 */

public class ContractListFragment<T> extends ListFragment {
    private T contract;

    @SuppressWarnings("unchecked")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            contract = (T) context;
        } catch (ClassCastException e) {
            throw new IllegalStateException(context.getClass().getSimpleName()
                    + " does not implement contract interface for " + getClass().getSimpleName(), e);
        }
    }

    @Override
    public void onDetach() {
        contract = null;
        super.onDetach();
    }

    public final T getContract() {
        return contract;
    }
}
