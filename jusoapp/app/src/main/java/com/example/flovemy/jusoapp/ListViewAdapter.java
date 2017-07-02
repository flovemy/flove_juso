package com.example.flovemy.jusoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by flovemy on 2017-06-26.
 *
 * 리스트 뷰와 리스트 뷰 아이템 객체를 연결해줄 어댑터를 정의한 클래스
 *
 */

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();


    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();


        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

        TextView index = (TextView) convertView.findViewById(R.id.indexView);
        TextView roadAddr = (TextView) convertView.findViewById(R.id.roadAddr);
        TextView engAddr = (TextView) convertView.findViewById(R.id.engAddr);
        TextView jibunAddr = (TextView) convertView.findViewById(R.id.jibunAddr);
        TextView zipNo = (TextView) convertView.findViewById(R.id.zipNo);

        ListViewItem listViewItem = listViewItemList.get(position);

        index.setText(listViewItem.getIndex() + "");
        roadAddr.setText(listViewItem.getroadAddr());
        engAddr.setText(listViewItem.getengAddr());
        jibunAddr.setText(listViewItem.getjibunAddr());
        zipNo.setText(listViewItem.getzipNo());


        return convertView;

    }



    public void addItem(JSONObject json) {
        ListViewItem item = new ListViewItem();

        try {
            item.setIndex(getCount() + 1);
            item.setroadAddr(json.getString("roadAddr"));
            item.setengAddr(json.getString("engAddr"));
            item.setjibunAddr(json.getString("jibunAddr") + " " + json.getString("bdNm"));
            item.setzipNo(json.getString("zipNo"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        listViewItemList.add(item);


    }

}
