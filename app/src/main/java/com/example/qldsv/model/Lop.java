package com.example.qldsv.model;

public class Lop {
    private String MaLop;
    private String TenLop;

    public Lop() {
    }

    public Lop(String maLop) {
        this.MaLop = maLop;
    }

    public Lop(String maLop, String tenLop) {
        this.MaLop = maLop;
        this.TenLop = tenLop;
    }

    public String getMaLop() {
        return MaLop;
    }

    public void setMaLop(String maLop) {
        this.MaLop = maLop;
    }

    public String getTenLop() {
        return TenLop;
    }

    public void setTenLop(String tenLop) {
        this.TenLop = tenLop;
    }
}
