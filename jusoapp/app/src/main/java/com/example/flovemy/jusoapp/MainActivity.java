package com.example.flovemy.jusoapp;

import android.support.v7.app.ActionBar;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import okhttp3.*;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by flovemy on 2017-06-26.
 */


public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button searchButton;
    ListView listview;
    ListViewAdapter adapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private Runnable updateUI = new Runnable() {
        public void run() {
            MainActivity.this.adapter.notifyDataSetChanged();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.title);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        setContentView(R.layout.activity_main);


        setContentView(R.layout.activity_main);

        this.editText = (EditText) findViewById(R.id.editText);
        this.searchButton = (Button) findViewById(R.id.button);
        this.listview = (ListView) findViewById(R.id.listview);

        this.searchButton.setOnClickListener(btnClickListener);

    }


    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String input = editText.getText().toString();


            adapter = new ListViewAdapter();
            listview.setAdapter(adapter);


            if (input.isEmpty()) {
                openSimpleAlertDialog("Error", "검색어를 입력해주세요.");
                return;
            }

            requestJuso();

            runOnUiThread(updateUI);
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    };

    private void requestJuso() {
        OkHttpClient client = new OkHttpClient();

        String url = "http://203.238.58.74:8983/app/search/addrSearchApi.do?";
        url = url + "keyword=" + editText.getText().toString() + "&resultType=json";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callbackGettingJuso);
    }

    private Callback callbackGettingJuso = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            openSimpleAlertDialogOnUiThread(
                    "Error",
                    "서버에 연결할 수 없거나 서버 오류가 있습니다."
            );
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String strJsonOutput = response.body().string();

            try {
                JSONObject jsonOutput = new JSONObject(strJsonOutput);
                final JSONArray jsonArray = jsonOutput.getJSONObject("results").getJSONArray("juso");
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        adapter.addItem(jsonArray.getJSONObject(i));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
                openSimpleAlertDialogOnUiThread("Error", "제이슨 값이 오지않았다. 없을리 없으니까 대충 씀.");
            }


        }
    };

    private void openSimpleAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void openSimpleAlertDialogOnUiThread(final String title, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openSimpleAlertDialog(title, message);
            }
        });
    }
}