package com.vph.qltb.ThietBi;

public class ModuleTB {
    String  tenthietbi, soluong, thongtin, hinhanh, loai, role;
    public ModuleTB(String tenthietbi, String soluong, String thongtin, String hinhanh, String loai, String role) {
        this.tenthietbi = tenthietbi;
        this.soluong = soluong;
        this.thongtin = thongtin;
        this.hinhanh = hinhanh;
        this.loai = loai;
        this.role = role;
    }
    public ModuleTB(String tenthietbi, String loai, String role){
        this.tenthietbi = tenthietbi;
        this.loai = loai;
        this.role = role;
    }
    public ModuleTB(){

    }
    public String getTenthietbi() {
        return tenthietbi;
    }

    public void setTenthietbi(String tenthietbi) {
        this.tenthietbi = tenthietbi;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getThongtin() {
        return thongtin;
    }

    public void setThongtin(String thongtin) {
        this.thongtin = thongtin;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
