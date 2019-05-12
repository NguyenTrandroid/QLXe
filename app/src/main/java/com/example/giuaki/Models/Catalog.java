package com.example.giuaki.Models;

public class Catalog {
    String id;
    String name;
    String country;
    int img;
    int imgOn;

    public Catalog(String id, String name, String country, int img, int imgOn) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.img = img;
        this.imgOn = imgOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getImgOn() {
        return imgOn;
    }

    public void setImgOn(int imgOn) {
        this.imgOn = imgOn;
    }
}
