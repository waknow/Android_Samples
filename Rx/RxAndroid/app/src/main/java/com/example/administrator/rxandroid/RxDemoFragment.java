package com.example.administrator.rxandroid;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/18.
 */

public class RxDemoFragment extends ListFragment {
    private static final String[] items = Items.getItems();
    private ArrayList<String> model = new ArrayList<>();
    private ArrayAdapter<String> adapter = null;
    private Disposable sub = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, model);

        Observable<String> observable = Observable
                .create(source())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Toast.makeText(getActivity(), R.string.done, Toast.LENGTH_LONG).show());
        sub = observable.subscribe(s -> adapter.add(s));
    }

    private ObservableOnSubscribe<String> source() {
        return (e -> {
            for (String item : items) {
                e.onNext(item);
                SystemClock.sleep(400);
            }
            e.onComplete();
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setScrollbarFadingEnabled(false);
        setListAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        if (sub != null && !sub.isDisposed()) {
            sub.isDisposed();
        }
        super.onDestroyView();
    }

}
