package com.example.flovemy.jusoapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by flovemy on 2017-06-26.
 */

public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private Intent intent;
    private TextView postNum;
    private TextView doroJuso;

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

        this.setContentView(R.layout.activity_main);
        postNum = (TextView) findViewById(R.id.postNum);
        doroJuso = (TextView) findViewById(R.id.doroJuso);
        searchButton = (Button) findViewById(R.id.button);
        //버튼에 이벤트 리스너를 추가한다.
        searchButton.setOnClickListener(btnClickListener);

        //리스트뷰에 스크롤 리스너를 추가한다.

    }


    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(MainActivity.this, SubActivity.class);
            startActivityForResult(intent, 3);

        }
    };

    private int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp - 1;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            doroJuso.setText(data.getStringExtra("roadAddr"));
            postNum.setText(data.getStringExtra("zipNo"));

            while (doroJuso.getLineCount() > 1) {



                doroJuso.setTextSize(TypedValue.COMPLEX_UNIT_DIP, pxToDp(this, (int) doroJuso.getTextSize()));


            }

        }
    }

}