package com.example.flovemy.jusoapp;

import android.support.v7.app.ActionBar;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
    int count;
    int pageIndex = 1;
    boolean lastItemVisibleFlag = false;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //Main Thread에서 UI 갱신하기 위한 Runnable
    private Runnable updateUI = new Runnable() {
        public void run() {
            TextView countView = (TextView) findViewById(R.id.countView);
            countView.setText(count + "");
            MainActivity.this.adapter.notifyDataSetChanged();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //커스텀 액션바 추가 부분
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.title);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        setContentView(R.layout.activity_main);

        this.editText = (EditText) findViewById(R.id.editText);
        this.searchButton = (Button) findViewById(R.id.button);
        this.listview = (ListView) findViewById(R.id.listview);


        //버튼에 이벤트 리스너를 추가한다.
        searchButton.setOnClickListener(btnClickListener);

        //리스트뷰에 스크롤 리스너를 추가한다.
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //사용자가 스크롤링을 멈추고 Flag가 트루일경우 = 리스트 마지막 아이템을 보고있을때
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {

                    requestJuso();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //리스트의 마지막인지 아닌지 확인한다.
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
        });
    }


    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pageIndex = 1;
            String input = editText.getText().toString();
            adapter = new ListViewAdapter();
            listview.setAdapter(adapter);
            if (input.isEmpty() || input.length() < 2) {
                openSimpleAlertDialog("Error", "검색어를 두 글자 이상 입력해주세요.");
                return;
            }
            //DB서버로 주소를 요청 //비동기
            requestJuso();
            //키패드를 닫는다.
            closeKeypad();
        }
    };

    //키패드를 강제로 닫는 메소드
    private void closeKeypad() {
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    //웹서버로 주소 정보를 요청 및 데이터를 리스트 뷰에 추가한다.
    private void requestJuso() {
        OkHttpClient client = new OkHttpClient();
        String url = "http://203.238.58.74:8983/app/search/addrSearchApi.do?";
        url = url + "keyword=" + editText.getText().toString() + "&resultType=json&currentPage=" + pageIndex++;

        Request request = new Request.Builder()
                .url(url)
                .build();
        //리퀘스트를 보내고 그 답신을 받아온다.
        client.newCall(request).enqueue(callbackGettingJuso);
    }

    //웹서버와 연결하고 데이터 값을 받아오는 콜백 객체
    private Callback callbackGettingJuso = new Callback() {

        //값을 가져오는데 실패
        @Override
        public void onFailure(Call call, IOException e) {
            openSimpleAlertDialogOnUiThread(
                    "Error",
                    "서버에 연결할 수 없거나 서버 오류가 있습니다."
            );
        }

        //값을 가져오는데 성공
        @Override
        public void onResponse(Call call, Response response) throws IOException {
            final String strJsonOutput = response.body().string();

            try {
                //제이슨 객체 생성
                JSONObject jsonOutput = new JSONObject(strJsonOutput);
                final JSONArray jsonArray = jsonOutput.getJSONObject("results").getJSONArray("juso");
                count = jsonOutput.getJSONObject("results").getJSONObject("common").getInt("totalCount");

                //객체 숫자 만큼 리스트 뷰 어댑터에 추가
                for (int i = 0; i < jsonArray.length(); i++) {

                    try {
                        adapter.addItem(jsonArray.getJSONObject(i));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //비동기이기 때문에 메인쓰레드를 불러 UI를 갱신
                runOnUiThread(updateUI);


            } catch (JSONException e) {
                e.printStackTrace();
                openSimpleAlertDialogOnUiThread("Error", "제이슨 값이 오지않았다. 없을리 없으니까 대충 씀.");

            }
        }
    };

    //경고알람을 띄우는 메소드
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

    //경고 알림시 실시간으로 화면 생신
    private void openSimpleAlertDialogOnUiThread(final String title, final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openSimpleAlertDialog(title, message);
            }
        });
    }
}