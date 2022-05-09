package com.vph.qltb.ThietBi;

public class ModuleTB {

    String  ten, soluong, thongtin, hinhanh, loai, id;
    String yeuthich;
    public ModuleTB(String ten, String soluong, String thongtin, String hinhanh, String loai, String id) {
        this.ten = ten;
        this.soluong = soluong;
        this.thongtin = thongtin;
        this.hinhanh = hinhanh;
        this.loai = loai;
        this.id = id;
    }

    public ModuleTB(){

    }

    public ModuleTB(String id, String ten){
        this.id = id;
        this.ten = ten;
    }


    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getId() {
        return id;
    }

    public void setId(String key) {
        this.id = key;
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
