package com.example.giuaki.Models;

public class Car {
    String maXe, maLoai, tenLoai, xuatXu, tenXe, dungTich, donGia;

    public Car(String maXe, String maLoai, String tenLoai, String xuatXu, String tenXe, String dungTich, String donGia) {
        this.maXe = maXe;
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.xuatXu = xuatXu;
        this.tenXe = tenXe;
        this.dungTich = dungTich;
        this.donGia = donGia;
    }

    public Car(String maXe, String maLoai, String tenXe, String dungTich, String donGia) {
        this.maXe = maXe;
        this.maLoai = maLoai;
        this.tenXe = tenXe;
        this.dungTich = dungTich;
        this.donGia = donGia;
    }

    public String getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    public void setXuatXu(String xuatXu) {
        this.xuatXu = xuatXu;
    }

    public String getMaXe() {
        return maXe;
    }

    public void setMaXe(String maXe) {
        this.maXe = maXe;
    }

    public String getTenXe() {
        return tenXe;
    }

    public void setTenXe(String tenXe) {
        this.tenXe = tenXe;
    }

    public String getDungTich() {
        return dungTich;
    }

    public void setDungTich(String dungTich) {
        this.dungTich = dungTich;
    }

    public String getDonGia() {
        return donGia;
    }

    public void setDonGia(String donGia) {
        this.donGia = donGia;
    }
}
