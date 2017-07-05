package com.jove.demo.websocket;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends Activity {

    private Button start;
    private TextView output;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button) findViewById(R.id.start);
        output = (TextView) findViewById(R.id.output);
        client = new OkHttpClient();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
                start.setEnabled(false);
            }
        });
    }

    private void start() {
        Request request = new Request.Builder()
                .url("ws://echo.websocket.org")
                .build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        client.dispatcher().executorService().shutdown();
    }

    private void output(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(String.format(Locale.CHINA, "%s\n%s: %s",
                        output.getText().toString(),
                        SimpleDateFormat.getTimeInstance(DateFormat.DEFAULT).format(new Date()),
                        text));
            }
        });
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            webSocket.send("Hello, it's SSaurel!");
            webSocket.send("What's up?");
            webSocket.send(ByteString.decodeHex("adadadad"));
            webSocket.close(NORMAL_CLOSURE_STATUS, "GOODBYE");
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            output("Receiving: " + text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            output("Receiving: " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            output("Closing: " + code + " / " + reason);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            output("Closed: " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            output("Error: " + t.getMessage());
        }
    }

}
