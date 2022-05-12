package com.vph.qltb.SinhVien;

public class ModuleSV {
    String sinhvien, lop,sdt,mssv,soluong,tenthietbi,hantra, tinhtrang,lydo ,id;
    String timeDK, timeMuon, timeTra;
    String dateDK, dateMuon, dateTra;
    //Đăng ký
    public ModuleSV(String soluong, String  tenthietbi, String dateDK, String timeDK, String tinhtrang, String lydo, String id) {
        this.timeDK = timeDK;
        this.soluong = soluong;
        this.tenthietbi = tenthietbi;
        this.dateDK = dateDK;
        this.tinhtrang = tinhtrang;
        this.lydo = lydo;
        this.id = id;
    }
    //Trả
    public ModuleSV(String soluong, String  tenthietbi, String dateDK, String timeDK, String dateMuon, String timeMuon, String tinhtrang, String lydo, String id) {
        this.timeMuon = timeMuon;
        this.timeDK = timeDK;
        this.dateMuon = dateMuon;
        this.soluong = soluong;
        this.tenthietbi = tenthietbi;
        this.dateDK = dateDK;
        this.tinhtrang = tinhtrang;
        this.lydo = lydo;
        this.id = id;
    }


    //Lịch sử trả + mượn
    public ModuleSV(String mssv, String soluong, String  tenthietbi, String dateDK, String timeDK, String dateMuon, String timeMuon, String dateTra, String timeTra,  String lydo, String id) {
        this.mssv = mssv;

        this.timeDK = timeDK;
        this.timeMuon = timeMuon;
        this.timeTra = timeTra;

        this.dateTra = dateTra;
        this.dateMuon = dateMuon;
        this.dateDK = dateDK;
        this.soluong = soluong;

        this.tenthietbi = tenthietbi;

        this.lydo = lydo;
        this.id = id;
    }
    public ModuleSV(){

    }
    public String getSinhvien() {
        return sinhvien;
    }

    public void setSinhvien(String sinhvien) {
        this.sinhvien = sinhvien;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
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

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getTenthietbi() {
        return tenthietbi;
    }

    public void setTenthietbi(String tenthietbi) {
        this.tenthietbi = tenthietbi;
    }

    public String getHantra() {
        return hantra;
    }

    public void setHantra(String hantra) {
        this.hantra = hantra;
    }

    public String getTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(String tinhtrang) {
        this.tinhtrang = tinhtrang;
    }

    public String getLydo() {
        return lydo;
    }

    public void setLydo(String lydo) {
        this.lydo = lydo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimeDK() {
        return timeDK;
    }

    public void setTimeDK(String timeDK) {
        this.timeDK = timeDK;
    }

    public String getTimeMuon() {
        return timeMuon;
    }

    public void setTimeMuon(String timeMuon) {
        this.timeMuon = timeMuon;
    }

    public String getTimeTra() {
        return timeTra;
    }

    public void setTimeTra(String timeTra) {
        this.timeTra = timeTra;
    }

    public String getDateDK() {
        return dateDK;
    }

    public void setDateDK(String dateDK) {
        this.dateDK = dateDK;
    }

    public String getDateMuon() {
        return dateMuon;
    }

    public void setDateMuon(String dateMuon) {
        this.dateMuon = dateMuon;
    }

    public String getDateTra() {
        return dateTra;
    }

    public void setDateTra(String dateTra) {
        this.dateTra = dateTra;
    }
}
