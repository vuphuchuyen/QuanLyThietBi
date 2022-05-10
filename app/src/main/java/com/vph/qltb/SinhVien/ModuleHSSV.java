package com.vph.qltb.SinhVien;

public class ModuleHSSV {
    String sinhvien, lop, sdt, mssv, role, matkhau;

    public ModuleHSSV(String sinhvien, String lop, String sdt, String mssv, String role, String matkhau) {
        this.sinhvien = sinhvien;
        this.lop = lop;
        this.sdt = sdt;
        this.mssv = mssv;
        this.role = role;
        this.matkhau = matkhau;
    }
    public ModuleHSSV() {

    }
    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }
}
