package com.example.qldsv.model;

public class TaiKhoan {

    private String MaTk;
    private String TenTaiKhoan;

    private String HoTen;
    private String MatKhau;

    public TaiKhoan() {
    }

    public TaiKhoan(String MaTk, String TenTaiKhoan) {
        this.MaTk = MaTk;
        this.TenTaiKhoan = TenTaiKhoan;

    }
    public TaiKhoan(String MaTk, String TenTaiKhoan, String MatKhau) {
        this.MaTk = MaTk;
        this.TenTaiKhoan = TenTaiKhoan;
        this.MatKhau = MatKhau;
    }

    public TaiKhoan(String MaTk, String TenTaiKhoan, String MatKhau, String HoTen) {
            this.MaTk = MaTk;
            this.TenTaiKhoan = TenTaiKhoan;
            this.MatKhau = MatKhau;
            this.HoTen = HoTen;
    }

    public String getMaTk() {
        return MaTk;
    }

    public void setMaTk(String MaTk) {
        this.MaTk = MaTk;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getTenTaiKhoan() {
        return TenTaiKhoan;
    }

    public void setTenTaiKhoan(String TenTaiKhoan) {
        this.TenTaiKhoan = TenTaiKhoan;
    }
    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String MatKhau) {
        this.MatKhau = MatKhau;
    }


}
