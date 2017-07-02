package com.example.flovemy.jusoapp;

/**
 * Created by flovemy on 2017-06-26.
 *
 * 리스트 뷰에 들어갈 아이템 객체를 정의한 클래스
 *
 */

public class ListViewItem {
    private int index;

    private String roadAddr;
    private String engAddr;
    private String jibunAddr;
    private String zipNo;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public String getroadAddr() {
        return roadAddr;
    }

    public void setroadAddr(String roadAddr) {
        this.roadAddr = roadAddr;
    }

    public String getengAddr() {
        return engAddr;
    }

    public void setengAddr(String engAddr) {
        this.engAddr = engAddr;
    }

    public String getjibunAddr() {
        return jibunAddr;
    }

    public void setjibunAddr(String jibunAddr) {
        this.jibunAddr = jibunAddr;
    }

    public String getzipNo() {
        return zipNo;
    }

    public void setzipNo(String zipNo) {
        this.zipNo = zipNo;
    }
}
