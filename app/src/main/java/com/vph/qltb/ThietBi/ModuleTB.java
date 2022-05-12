package com.vph.qltb.ThietBi;

public class ModuleTB {

    String  ten, soluong, thongtin, hinhanh, loai, role;
    public ModuleTB(String ten, String soluong, String thongtin, String hinhanh, String loai, String role) {
        this.ten = ten;
        this.soluong = soluong;
        this.thongtin = thongtin;
        this.hinhanh = hinhanh;
        this.loai = loai;
        this.role = role;
    }
    public ModuleTB(String ten, String loai, String role){
        this.ten = ten;
        this.loai = loai;
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ModuleTB(){

    }
    public ModuleTB(String ten){
        this.ten = ten;
    }


    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
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
