package com.example.animapedia;

public class FavoriteAnimal {
    private int id;
    private String name;
    private String image;

    // لا تحذف هذا الكونستركتور الفارغ، Firebase يحتاجه
    public FavoriteAnimal() {
    }

    public FavoriteAnimal(int id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getImage() { return image; }
}