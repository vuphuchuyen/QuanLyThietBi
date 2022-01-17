package com.vph.qltb.ThietBi;

public class ModuleTB {
    String  ten, soluong, thongtin, hinhanh;
    public ModuleTB(String ten, String soluong, String thongtin, String hinhanh) {
        this.ten = ten;
        this.soluong = soluong;
        this.thongtin = thongtin;
        this.hinhanh = hinhanh;
    }



    public ModuleTB() {
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public void setThongtin(String thongtin) {
        this.thongtin = thongtin;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }



    public String getTen() {
        return ten;
    }

    public String getSoluong() {
        return soluong;
    }

    public String getThongtin() {
        return thongtin;
    }

    public String getHinhanh() {
        return hinhanh;
    }
}
