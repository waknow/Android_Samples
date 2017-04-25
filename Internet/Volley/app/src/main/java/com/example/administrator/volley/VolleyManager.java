package com.example.administrator.volley;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2017/4/25.
 */

public class VolleyManager {
    private static volatile VolleyManager INSTANCE;
    private final RequestQueue queue;
    private final ImageLoader imageLoader;

    synchronized static VolleyManager get(Context ctx) {
        if (INSTANCE == null) {
            INSTANCE = new VolleyManager(ctx.getApplicationContext());
        }
        return INSTANCE;
    }

    private VolleyManager(Context ctx) {
        queue = Volley.newRequestQueue(ctx);
        imageLoader = new ImageLoader(queue, new LruBitmapCache(ctx));
    }

    void enqueue(Request<?> request) {
        queue.add(request);
    }

    void loadImage(String url, ImageView v, int placeHolderDrawable, int errDrawable) {
        imageLoader.get(url,
                ImageLoader.getImageListener(v, placeHolderDrawable, errDrawable));
    }

}
