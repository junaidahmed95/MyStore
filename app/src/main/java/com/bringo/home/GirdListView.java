package com.bringo.home;

public class GirdListView {
    String txtv1,txtv2,txtv3;
    int img;

    public String getTxtv1() {
        return txtv1;
    }

    public void setTxtv1(String txtv1) {
        this.txtv1 = txtv1;
    }

    public String getTxtv2() {
        return txtv2;
    }

    public void setTxtv2(String txtv2) {
        this.txtv2 = txtv2;
    }

    public String getTxtv3() {
        return txtv3;
    }

    public void setTxtv3(String txtv3) {
        this.txtv3 = txtv3;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public GirdListView(String txtv1, String txtv2, String txtv3, int img) {
        this.txtv1 = txtv1;
        this.txtv2 = txtv2;
        this.txtv3 = txtv3;
        this.img = img;
    }
}
