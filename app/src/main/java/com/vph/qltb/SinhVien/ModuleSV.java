package com.vph.qltb.SinhVien;

public class ModuleSV {
    String sinhvien, lop,sdt,mssv,soluong,tenthietbi,ngaymuon,hantra;

    public ModuleSV(String sinhvien, String lop, String sdt, String mssv, String soluong, String tenthietbi, String ngaymuon, String hantra) {
        this.sinhvien = sinhvien;
        this.lop = lop;
        this.sdt = sdt;
        this.mssv = mssv;
        this.soluong = soluong;
        this.tenthietbi = tenthietbi;
        this.ngaymuon = ngaymuon;
        this.hantra = hantra;
    }

    public ModuleSV() {
    }

    public String getSinhvien() {
        return sinhvien;
    }

    public void setSinhvien(String sinhvien) {
        this.sinhvien = sinhvien;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
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

    public String getNgaymuon() {
        return ngaymuon;
    }

    public void setNgaymuon(String ngaymuon) {
        this.ngaymuon = ngaymuon;
    }

    public String getHantra() {
        return hantra;
    }

    public void setHantra(String hantra) {
        this.hantra = hantra;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }
}