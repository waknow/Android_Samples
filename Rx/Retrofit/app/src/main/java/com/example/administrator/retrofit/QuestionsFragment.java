package com.example.administrator.retrofit;

import android.app.ListFragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.retrofit.databinding.RowBinding;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/4/25.
 */

public class QuestionsFragment extends ListFragment {

    public interface Contract {
        void onQuestionClicked(Question question);
    }

    private ArrayList<Question> questions = new ArrayList<>();
    private HashMap<String, Question> questionMap = new HashMap<>();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.stackexchange.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    StackOverflowInterface so = retrofit.create(StackOverflowInterface.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);

        so.questions("android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                    for (Item item : result.items) {
                        Question question = new Question(item);
                        questions.add(question);
                        questionMap.put(question.id, question);
                    }
                    setListAdapter(new QuestionAdapter(questions));
                }, e -> {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(getClass().getSimpleName(), "Exception from Retrofit request to StackOverflow", e);
                });
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            updateQuestions();
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateQuestions() {
        ArrayList<String> idList = new ArrayList<>();
        for (Question question : questions) {
            idList.add(question.id);
        }
        String ids = TextUtils.join(";", idList);
        so.update(ids)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(result -> {
                    for (Item item : result.items) {
                        Question question = questionMap.get(item.id);
                        if (question != null) {
                            question.updateFrom(item);
                        }
                    }
                }, e -> {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(getClass().getSimpleName(), "Exception from Retrofit request to StackOverflow", e);
                });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Question question = ((QuestionAdapter) getListAdapter()).getItem(position);
        ((Contract) getActivity()).onQuestionClicked(question);
    }

    private class QuestionAdapter extends ArrayAdapter<Question> {

        QuestionAdapter(List<Question> items) {
            super(getActivity(), R.layout.row, R.id.title, items);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            RowBinding rowBinding = DataBindingUtil.getBinding(convertView);
            if (rowBinding == null) {
                rowBinding = RowBinding.inflate(getActivity().getLayoutInflater(), parent, false);
            }
            Question question = getItem(position);
            rowBinding.setQuestion(question);
            if (question != null) {
                ImageView icon = rowBinding.icon;
                Picasso.with(getActivity())
                        .load(question.owner.profileImage)
                        .fit().centerCrop()
                        .placeholder(R.drawable.owner_placeholder)
                        .error(R.drawable.owner_error).into(icon);
            } else {
                Log.w(getClass().getSimpleName(), String.format("question %d is null", position));
            }
            return rowBinding.getRoot();
        }
    }

}
