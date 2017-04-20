package com.example.administrator.editorfragment;

import android.app.Fragment;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by Administrator on 2017/4/20.
 */

public class EditorFragment extends LifecycleLoggingFragment {
    private static final String KEY_FILE = "keyFile";
    private EditText editor;
    private LoadTextTask loadTask = null;
    private boolean loaded = false;

    static EditorFragment newInstance(File fileToEdit) {
        EditorFragment frag = new EditorFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_FILE, fileToEdit);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onPause() {
        if (loaded) {
            new SaveThread(getActivity().getApplicationContext(), editor.getText().toString(),
                    (File) getArguments().getSerializable(KEY_FILE))
                    .start();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (loadTask != null) {
            loadTask.cancel(false);
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.editor, container, false);
        editor = (EditText) result.findViewById(R.id.editor);
        return result;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!loaded) {
            loadTask = new LoadTextTask();
            loadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                    (File) getArguments().getSerializable(KEY_FILE));
        }
    }

    private class LoadTextTask extends AsyncTask<File, Void, String> {

        @Override
        protected String doInBackground(File... files) {
            String result = null;
            if (files[0].exists()) {
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(files[0]));
                    try {
                        StringBuilder sb = new StringBuilder();
                        String line = br.readLine();
                        while (line != null) {
                            sb.append(line);
                            sb.append("\n");
                            line = br.readLine();
                        }
                        result = sb.toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        br.close();
                    }
                } catch (IOException e) {
                    Log.e(getClass().getSimpleName(), "Exception reading file", e);
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            editor.setText(s);
            loadTask = null;
            loaded = true;
        }
    }

    private static class SaveThread extends Thread {
        private String text;
        private File fileToEdit;
        private Context ctx;

        public SaveThread(Context ctx, String text, File fileToEdit) {
            this.ctx = ctx;
            this.text = text;
            this.fileToEdit = fileToEdit;
        }

        @Override
        public void run() {
            try {
                boolean isSuccess = this.fileToEdit.getParentFile().mkdirs();
                if (!isSuccess) {
                    Log.e(getClass().getSimpleName(), "Make dir for parent files failed");
                }
                FileOutputStream fos = new FileOutputStream(this.fileToEdit);
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(fos))) {
                    writer.write(this.text);
                    writer.flush();
                    fos.getFD().sync();
                } finally {
                    String[] paths = new String[]{fileToEdit.getAbsolutePath()};
                    MediaScannerConnection.scanFile(ctx, paths, null, null);
                }
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), "Exception writing file", e);
            }
        }
    }
}
